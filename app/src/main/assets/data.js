Page({
  data: {
    test: 'Hello World',
    test1: 'Hello World12',
    test2: 'Hello World13',
    list: [1,2,3]
  },
  tap: function() {
    page.setData('test', 'aaaaaaaa')
  },
  tap1: function() {
    page.setData('test1', 'bbbbbbbb')
  }
})