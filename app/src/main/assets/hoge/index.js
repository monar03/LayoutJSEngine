Page({
  data: {
  },
  tap: function(data) {
    aquagear.navigateTo('/test')
  },
  tap1: function() {
    aquagear.showToast('test')
    page.setData('list', [
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
})