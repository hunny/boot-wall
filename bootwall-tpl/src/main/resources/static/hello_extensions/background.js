chrome.runtime.onInstalled.addListener(function() {
  console.log("The Application Has been Installed.");
  chrome.storage.local.set({
    color: '#3aa757'
  }, function(result) {
    console.log('Value is set to color. ');
  });
  chrome.browserAction.setBadgeText({
    text: 'ON'
  });
  chrome.browserAction.setBadgeBackgroundColor({
    color: '#4688F1'
  });
});
chrome.storage.onChanged.addListener(function(changes, namespace) {
  for (key in changes) {
    var storageChange = changes[key];
    console.log('Storage key "%s" in namespace "%s" changed. '
            + 'Old value was "%s", new value is "%s".', key, namespace,
            storageChange.oldValue, storageChange.newValue);
  }
});
chrome.browserAction.onClicked.addListener(function(tab) {
  console.log('browser action onclicked.');
  chrome.tabs.executeScript({
    code: 'document.body.style.backgroundColor="red"'
  });
});