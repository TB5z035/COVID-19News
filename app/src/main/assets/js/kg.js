function inArray(arr, x) {
    return arr.findIndex((xx) => {
        return (xx === x.url)
    }) !== -1;
}

function searchEnt(label, callback) {
    console.log('https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=' + label);
    (fetch('https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=' + label)
        .then(response => response.json())
        .then(data => callback(data)));
}

function parseEnt(data) {
    // var hot = data.hot;
    var label = data.label;
    var url = data.url;
    // var img = data.img;
    // return {hot, label, url, img};
    return { url, label };
}

function parseEdges(data) {
    var detailed = data.abstractInfo.COVID.relations;
    detailed = detailed.sort(function (a, b) { return 0.5 - Math.random(); });
    var cnt = 0, Nmax = 20;
    var ent1 = parseEnt(data);
    var edges = detailed.map((x) => {
        var r = x.relation;
        var ent2 = { url: x.url, label: x.label };
        if (x.forward === true) {
            return [ent1, r, ent2];
        } else {
            if (cnt < Nmax) {
                cnt++;
                return [ent2, r, ent1];
            } else {
                cnt++;
                return null;
            }
        }
    });
    return edges.filter((x) => (x !== null));
}
