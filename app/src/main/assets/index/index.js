{
  data: {
    list: [
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World1',
            test1: 'Hello World2',
            test2: 'Hello World3',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World4',
            test1: 'Hello World5',
            test2: 'Hello World6',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World4',
            test1: 'Hello World5',
            test2: 'Hello World6',
        },
        {
            url: "https://avatars1.githubusercontent.com/u/2632317?s=460&v=4",
            test: 'Hello World7',
            test1: 'Hello World8',
            test2: 'Hello World9',
        }]
  },
  tap: function(data) {
    aquagear.navigateTo('hoge')
  },
  tap1: function() {
    aquagear.showToast('test')
    this.setData('list', [
    {
      test: 'Hello World1',
      test1: 'Hello World2',
      test2: 'Hello World3'
    },
    {
      test: 'Hello World10',
      test1: 'Hello World20',
      test2: 'Hello World30'
    }])
  }
}