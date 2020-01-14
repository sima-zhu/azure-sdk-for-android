package com.azure.android.core.util.tokenrequest;

import com.azure.android.core.credential.AccessToken;

import org.threeten.bp.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A Handle that a background running thread (e.g. okhttp thread) uses to synchronously receives an
 * access-token produced by the main UI thread. A background running thread can initiate the
 * access-token request by calling {@link TokenRequestObservable#sendRequest(List)} ()}, which returns
 * {@link TokenRequestHandle} instance. The background thread then wait for the arrival
 * of the access-token by calling {@link TokenRequestHandle#waitForToken(Duration)}.
 *
 * IMPLEMENTATION NOTE: The {@link TokenRequestObservable} INTERNALLY uses the same {@link TokenRequestHandle}
 * object (returned from sendRequest) as an Event to notify a {@link TokenRequestObserver} about the need
 * for access-token. Upon receiving this event, the {@link TokenRequestObserver} invokes
 * {@link TokenRequestObserver#onTokenRequest(String[], TokenResponseCallback)} (TokenResponseCallback)}
 * which is responsible for retrieving the access token, the observer then sets the access-token or
 * access-token retrieval error in the Handle using it's package PRIVATE setters
 * {@link TokenRequestHandle#setToken(AccessToken)}, {@link TokenRequestHandle#setError(Throwable)}.
 *
 * It is important that an Event instance is processed only once by the {@link TokenRequestObserver}.
 * The {@link TokenRequestObservable} uses {@link androidx.lifecycle.LiveData} to post the Event to
 * {@link TokenRequestObserver}, the LiveData is designed to caches the latest value (in this case Event)
 * and re-deliver when there is a new observer. An example for such unexpected re-delivery is, Let's say
 * the {@link TokenRequestObservable} is owned by a {@link androidx.lifecycle.ViewModel}, the ViewModel
 * live through multiple Activity::onCreate - Activity::onDestroy spans
 * https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle.
 * Let's say in Activity::onCreate(Bundle) we register a {@link TokenRequestObserver} to
 * this ViewModel scoped Observable. When a background thread request for token, the observer receives
 * the request and process it. If user begins rotating the screen (i.e configuration changed) then
 * the Activity::onDestroy() is called and once the rotation is done the Activity::onCreate(Bundle) method
 * will be called again, which result in registration of new {@link TokenRequestObserver} and internally
 * LiveData re-deliver the old cached/handled event, which we definitely don't want the new Observer to handle.
 * To identify such an Event, the Observer uses {@link TokenRequestHandle#isConsumed()}.
 */
class TokenRequestHandle {
    // The scope of the token requested by the handle owner.
    private final List<String> scopes;
    // Indicates whether the handle is already consumed by a TokenRequestObserver.
    private boolean isConsumed;
    // The latch that signals the handle owner that token acquisition operation is completed (successfully or error-ed).
    private CountDownLatch latch = new CountDownLatch(1);
    // The retrieved access token.
    private AccessToken token;
    // The error on token retrieval.
    private Throwable error;

    /**
     * Creates TokenRequestHandle.
     *
     * @param scopes the requested token scope
     */
    public TokenRequestHandle(List<String> scopes) {
        this.scopes = new ArrayList<>(scopes);
    }

    /**
     * synchronously wait for the arrival of access-token requested via {@link TokenRequestObservable#sendRequest(List)} ()}.
     *
     * @param timeout the maximum time to wait in milliseconds
     * @return the access token
     * @throws Throwable
     */
    public AccessToken waitForToken(Duration timeout) throws Throwable {
        this.latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (this.error != null) {
            throw this.error;
        } else {
            return this.token;
        }
    }

    /**
     * PACKAGE PRIVATE METHOD.
     *
     * @return the scopes required for the token
     *
     */
    public List<String> getScopes() {
        return this.scopes;
    }

    /**
     * PACKAGE PRIVATE METHOD.
     *
     * @return true if event is consumed, false otherwise
     *
     */
    public boolean isConsumed() {
        if (this.isConsumed) {
            return true;
        } else {
            this.isConsumed = true;
            return false;
        }
    }

    /**
     * PACKAGE PRIVATE METHOD.
     *
     * Sets the access-token retrieved and unblock the background thread waiting
     * in {@link TokenRequestHandle#waitForToken(Duration)}.
     *
     * @param token the access-token
     */
    public void setToken(AccessToken token) {
        if (this.token != null) {
            throw new IllegalStateException("token is already set.");
        }
        this.token = token;
        this.latch.countDown();
    }

    /**
     * PACKAGE PRIVATE METHOD.
     *
     * Sets the error during access-token retrieval and unblock the background thread
     * waiting in {@link TokenRequestHandle#waitForToken(Duration)}.
     *
     * @param error the error
     */
    public void setError(Throwable error) {
        if (this.error != null) {
            throw new IllegalStateException("error is already set.");
        }
        this.error = error;
        this.latch.countDown();
    }
}