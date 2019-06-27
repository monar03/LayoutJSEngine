class Page {
    constructor(obj) {
        this.obj = obj
    }

    setData(key, data) {
        this.obj.data[key] = data
        aquagear.update(key)
    }

    getData(key) {
        var ret = {}
        ret[key] = this.obj.data[key]
        return JSON.stringify(ret)
    }

    onTap(funcName, data) {
        this.obj[funcName](data)
    }
}
