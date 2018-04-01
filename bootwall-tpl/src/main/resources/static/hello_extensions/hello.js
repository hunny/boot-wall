// btn btn_send

/*
 * var button = $(".btn_send"); button.on('click', function() {
 * console.log("监测到点击哈。"); });
 */
console.log("我在运行。");

chrome.extension.onMessage.addListener(
function(request, sender, sendResponse) {
  console.log("检测有请求。");
 // console.log("msg----------contentscript.js" + request.greeting);
});