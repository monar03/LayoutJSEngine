class Page {
    constructor(data) {
        this.prototype = data;
    }

    setData(key, data) {
        this.data[key] = data;
        aquagear.update()
    }

    getData(key) {
        return this.data[key];
    }
}
