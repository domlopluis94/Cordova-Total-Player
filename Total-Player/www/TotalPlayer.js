var exec = require('cordova/exec');

exports.playVlc = function (url, success, error) {
    exec(success, error, 'TotalPlayer', 'playVlc', [url]);
};
exports.exoPlayer = function (url, success, error) {
    exec(success, error, 'TotalPlayer', 'exoPlayer', [url]);
};
exports.mediaPlayer = function (url, success, error) {
    exec(success, error, 'TotalPlayer', 'mediaPlayer', [url]);
};