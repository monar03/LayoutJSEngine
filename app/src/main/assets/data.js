Page({
  data: {
    test: 'Hello World',
    test1: 'Hello World12'
  },
  tap: function() {
    page.setData('test', 'aaaaaaaa')
  },
  tap1: function() {
    page.setData('test1', 'bbbbbbbb')
  }
})