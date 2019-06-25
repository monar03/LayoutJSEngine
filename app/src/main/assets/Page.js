class Page {
    constructor(obj) {
        this.obj = obj
    }

    setData(key, data) {
        this.obj.data[key] = data
        aquagear.update(key)
    }

    getData(key) {
        return this.obj.data[key]
    }

    onTap(funcName) {
        this.obj[funcName]()
    }
}
