class Module {
    constructor(name, obj) {
        this.name = name
        console.log("constructor: name = " + this.name + "; obj = " + JSON.stringify(obj))
        for(var key in obj) {
            eval("this."+key+" = obj[key]")
        }
    }

    setData(key, data) {
        console.log("setData: key = " + key + "; data = " + JSON.stringify(data))
        this.data[key] = data
        aquagear.update(this.name, key)
    }

    getData(key) {
        var ret = {}
        ret[key] = this.data[key]
        console.log("getData: key = " + key + "; data = " + JSON.stringify(ret[key]))
        return JSON.stringify(ret)
    }

    onTap(funcName, data) {
        this[funcName](data)
    }
}
