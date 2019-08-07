var exec = require('cordova/exec');

function NyanPreference() {

}

NyanPreference.prototype.get = function(key) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, 'NyanPreference', 'get', [ key ]);
    });
}
NyanPreference.prototype.getAll = function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, 'NyanPreference', 'getAll', [ ]);
    });
}
NyanPreference.prototype.set = function(key, value) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, 'NyanPreference', 'set', [ key, value ]);
    });
}
NyanPreference.prototype.remove = function(key) {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, 'NyanPreference', 'remove', [ key ]);
    });
}
NyanPreference.prototype.clear = function() {
    return new Promise(function(resolve, reject) {
        exec(resolve, reject, 'NyanPreference', 'clear', [ ]);
    });
}

module.exports = new NyanPreference();
