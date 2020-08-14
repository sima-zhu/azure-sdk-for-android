 #!/usr/bin/env pwsh -c

<#
.DESCRIPTION
Creates a GitHub pull request for a given branch if it doesn't already exist
.PARAMETER RepoOwner
The GitHub repository owner to create the pull request against.
.PARAMETER RepoName
The GitHub repository name to create the pull request against.
.PARAMETER BaseBranch
The base or target branch we want the pull request to be against.
.PARAMETER PROwner
The owner of the branch we want to create a pull request for.
.PARAMETER PRBranch
The branch which we want to create a pull request for.
.PARAMETER AuthToken
A personal access token
#>
[CmdletBinding(SupportsShouldProcess = $true)]
param(
  [Parameter(Mandatory = $true)]
  $RepoOwner,

  [Parameter(Mandatory = $true)]
  $RepoName,

  [Parameter(Mandatory = $true)]
  $BaseBranch,

  [Parameter(Mandatory = $true)]
  $PROwner,

  [Parameter(Mandatory = $true)]
  $PRBranch,

  [Parameter(Mandatory = $true)]
  $AuthToken,

  [Parameter(Mandatory = $true)]
  $PRTitle,
  $PRBody = $PRTitle,

  $PRLabel
)

$headers = @{
  Authorization = "bearer $AuthToken"
}

$query = "state=open&head=${PROwner}:${PRBranch}&base=${BaseBranch}"

try {
  $resp = Invoke-RestMethod -Headers $headers "https://api.github.com/repos/$RepoOwner/$RepoName/pulls?$query"
}
catch { 
  Write-Error "Invoke-RestMethod [https://api.github.com/repos/$RepoOwner/$RepoName/pulls?$query] failed with exception:`n$_"
  exit 1
}
$resp | Write-Verbose

if ($resp.Count -gt 0) {
    Write-Host -f green "Pull request already exists $($resp[0].html_url)"

    # setting variable to reference the pull request by number
    Write-Host "##vso[task.setvariable variable=Submitted.PullRequest.Number]$($resp[0].number)"
}
else {
  $data = @{
    title                 = $PRTitle
    head                  = "${PROwner}:${PRBranch}"
    base                  = $BaseBranch
    body                  = $PRBody
    maintainer_can_modify = $true
    labels                = $PRLabel
  }

  try {
    $resp = Invoke-RestMethod -Method POST -Headers $headers `
                              "https://api.github.com/repos/$RepoOwner/$RepoName/pulls" `
                              -Body ($data | ConvertTo-Json)                      
  }
  catch {
    Write-Error "Invoke-RestMethod [https://api.github.com/repos/$RepoOwner/$RepoName/pulls] failed with exception:`n$_"
    exit 1
  }

  $resp | Write-Verbose
  Write-Host -f green "Pull request created https://github.com/$RepoOwner/$RepoName/pull/$($resp.number)"

  # setting variable to reference the pull request by number
  Write-Host "##vso[task.setvariable variable=Submitted.PullRequest.Number]$($resp.number)"

  # Add labels to the pull request
  if ($data["labels"] -ne "") {
    $issue_url = $resp.issue_url
    try {
      $resp = Invoke-RestMethod -Method PATCH -Headers $headers $issue_url -Body ($data | ConvertTo-Json)
    }
    catch {
      Write-Error "Invoke-RestMethod $issue_url failed with exception:`n$_"
      exit 1
    }

    $resp | Write-Verbose
    Write-Host -f green "Label added to pull request: https://github.com/$RepoOwner/$RepoName/pull/$($resp.number)"
  }
}
