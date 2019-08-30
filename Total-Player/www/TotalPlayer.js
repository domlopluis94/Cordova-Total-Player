var exec = require('cordova/exec');

exports.playVlc = function (url, success, error) {
	try{
		cordova.exec(success, error, 'TotalPlayer', 'playVlc', [url]);
	}catch(error){
		console.log(error);
	}
};
exports.exoPlayer = function (url, success, error) {
    exec(success, error, 'TotalPlayer', 'exoPlayer', [url]);
};
exports.mediaPlayer = function (url, success, error) {
    exec(success, error, 'TotalPlayer', 'mediaPlayer', [url]);
};