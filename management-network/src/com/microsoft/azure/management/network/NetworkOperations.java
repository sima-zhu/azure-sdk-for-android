/**
 * 
 * Copyright (c) Microsoft and contributors.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

// Warning: This code was generated by a tool.
// 
// Changes to this file may cause incorrect behavior and will be lost if the
// code is regenerated.

package com.microsoft.azure.management.network;

import com.microsoft.azure.core.OperationResponse;
import com.microsoft.azure.core.OperationStatusResponse;
import com.microsoft.azure.exception.ServiceException;
import com.microsoft.azure.management.network.models.NetworkGetConfigurationResponse;
import com.microsoft.azure.management.network.models.NetworkListResponse;
import com.microsoft.azure.management.network.models.NetworkSetConfigurationParameters;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParserException;

/**
* The Network Management API includes operations for managing the virtual
* networks for your subscription.  (see
* http://msdn.microsoft.com/en-us/library/windowsazure/jj157182.aspx for more
* information)
*/
public interface NetworkOperations {
    /**
    * The Begin Setting Network Configuration operation asynchronously
    * configures the virtual network.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157181.aspx for
    * more information)
    *
    * @param parameters Required. Parameters supplied to the Set Network
    * Configuration operation.
    * @throws MalformedURLException Thrown in case of an invalid request URL
    * @throws ProtocolException Thrown if invalid request method
    * @throws ServiceException Thrown if an unexpected response is found.
    * @throws IOException Signals that an I/O exception of some sort has
    * occurred
    * @return A standard service response including an HTTP status code and
    * request ID.
    */
    OperationResponse beginSettingConfiguration(NetworkSetConfigurationParameters parameters) throws MalformedURLException, ProtocolException, ServiceException, IOException;
    
    /**
    * The Begin Setting Network Configuration operation asynchronously
    * configures the virtual network.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157181.aspx for
    * more information)
    *
    * @param parameters Required. Parameters supplied to the Set Network
    * Configuration operation.
    * @return A standard service response including an HTTP status code and
    * request ID.
    */
    Future<OperationResponse> beginSettingConfigurationAsync(NetworkSetConfigurationParameters parameters);
    
    /**
    * The Get Network Configuration operation retrieves the network
    * configuration file for the given subscription.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157196.aspx for
    * more information)
    *
    * @throws MalformedURLException Thrown in case of an invalid request URL
    * @throws ProtocolException Thrown if invalid request method
    * @throws ServiceException Thrown if an unexpected response is found.
    * @throws IOException Signals that an I/O exception of some sort has
    * occurred
    * @return The Get Network Configuration operation response.
    */
    NetworkGetConfigurationResponse getConfiguration() throws MalformedURLException, ProtocolException, ServiceException, IOException;
    
    /**
    * The Get Network Configuration operation retrieves the network
    * configuration file for the given subscription.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157196.aspx for
    * more information)
    *
    * @return The Get Network Configuration operation response.
    */
    Future<NetworkGetConfigurationResponse> getConfigurationAsync();
    
    /**
    * The List Virtual network sites operation retrieves the virtual networks
    * configured for the subscription.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157185.aspx for
    * more information)
    *
    * @throws MalformedURLException Thrown in case of an invalid request URL
    * @throws ProtocolException Thrown if invalid request method
    * @throws ServiceException Thrown if an unexpected response is found.
    * @throws IOException Signals that an I/O exception of some sort has
    * occurred
    * @throws XmlPullParserException This exception is thrown to signal XML
    * Pull Parser related faults.
    * @throws DatatypeConfigurationException Invalid datatype configuration
    * @return The response structure for the Network Operations List operation.
    */
    NetworkListResponse list() throws MalformedURLException, ProtocolException, ServiceException, IOException, XmlPullParserException, DatatypeConfigurationException;
    
    /**
    * The List Virtual network sites operation retrieves the virtual networks
    * configured for the subscription.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157185.aspx for
    * more information)
    *
    * @return The response structure for the Network Operations List operation.
    */
    Future<NetworkListResponse> listAsync();
    
    /**
    * The Set Network Configuration operation asynchronously configures the
    * virtual network.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157181.aspx for
    * more information)
    *
    * @param parameters Required. Parameters supplied to the Set Network
    * Configuration operation.
    * @throws InterruptedException Thrown when a thread is waiting, sleeping,
    * or otherwise occupied, and the thread is interrupted, either before or
    * during the activity. Occasionally a method may wish to test whether the
    * current thread has been interrupted, and if so, to immediately throw
    * this exception. The following code can be used to achieve this effect:
    * @throws ExecutionException Thrown when attempting to retrieve the result
    * of a task that aborted by throwing an exception. This exception can be
    * inspected using the Throwable.getCause() method.
    * @throws ServiceException Thrown if the server returned an error for the
    * request.
    * @return The response body contains the status of the specified
    * asynchronous operation, indicating whether it has succeeded, is
    * inprogress, or has failed. Note that this status is distinct from the
    * HTTP status code returned for the Get Operation Status operation itself.
    * If the asynchronous operation succeeded, the response body includes the
    * HTTP status code for the successful request. If the asynchronous
    * operation failed, the response body includes the HTTP status code for
    * the failed request, and also includes error information regarding the
    * failure.
    */
    OperationStatusResponse setConfiguration(NetworkSetConfigurationParameters parameters) throws InterruptedException, ExecutionException, ServiceException;
    
    /**
    * The Set Network Configuration operation asynchronously configures the
    * virtual network.  (see
    * http://msdn.microsoft.com/en-us/library/windowsazure/jj157181.aspx for
    * more information)
    *
    * @param parameters Required. Parameters supplied to the Set Network
    * Configuration operation.
    * @return The response body contains the status of the specified
    * asynchronous operation, indicating whether it has succeeded, is
    * inprogress, or has failed. Note that this status is distinct from the
    * HTTP status code returned for the Get Operation Status operation itself.
    * If the asynchronous operation succeeded, the response body includes the
    * HTTP status code for the successful request. If the asynchronous
    * operation failed, the response body includes the HTTP status code for
    * the failed request, and also includes error information regarding the
    * failure.
    */
    Future<OperationStatusResponse> setConfigurationAsync(NetworkSetConfigurationParameters parameters);
}