function unstringify(jsonString) {
    return JSON.parse(jsonString);
}

function inArray(arr, x) {
    return arr.findIndex((xx) => (xx === x)) !== -1;
}

function getLatest(json) {
    var newjson = {};
    for (var location in json) {
        var data = json[location].data;
        data = data[data.length - 1];
        newjson[location] = [data[0], data[2], data[3]];
    }
    return newjson;
}

function getAll(json) {
    var newjson = {};
    for (var location in json) {
        var data = json[location].data;
        data = data.map((x) => [x[0], x[2], x[3]]);
        newjson[location] = data;
    }
    return newjson;
}

function getCountry(location) {
    var result = /^([^\|]+)\|?/g.exec(location);
    return result ? result[1] : null;
}

function getCounty(location) {
    var result = /\|([^\|]+)$/g.exec(location);
    return result ? result[1] : null;
}

function getAllcountry(json) {
    countries = []
    for (var location in json) {
        var country = getCountry(location);
        if (!inArray(countries, country)) {
            countries.push(country);
        }
    }
    return countries;
}

function mergeCountry(json) {
    var newjson = json;
    function accumulate(newjson, json, country, loc) {
        try {
            for (var i = 0; i < newjson[country].length; i++) {
                newjson[country][i] += json[loc][i];
            }
        } catch (error) {
            for (var i = 0; i < newjson[country].length; i++) {
                var data = newjson[country][i];
                for (var j = 0; j < data.length; j++) {
                    newjson[country][i][j] += json[loc][i][j];
                }
            }
        }
    }
    for (var loc in json) {
        var country = getCountry(loc);
        if (!(country in json)) {
            if (country in newjson) {
                accumulate(newjson, json, country, loc);
            }
            else {
                newjson[country] = json[loc];
            }
        }
    }
    return newjson;
}

// console.log(JSON.stringify(mergeCountry(getLatest(data))));
// console.log(getLatest(data)["United States of America"]);
// console.log(JSON.stringify(mergeCountry(getAll(data))));
