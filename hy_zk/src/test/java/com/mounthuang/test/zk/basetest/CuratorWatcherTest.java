package com.mounthuang.test.zk.basetest;

/**
 * Author: mounthuang
 * Data: 2017/5/11.
 */
public class CuratorWatcherTest {
//    private static final String zkAddr = "127.0.0.1:2181";
//    private static final String zkPath = "/zkTest";
//
//    CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddr, new RetryNTimes(10, 5000));
//    PathChildrenCache watcher;
//
//    @Before
//    public void before() {
//        client.start();
//        System.out.println("zk client start success");
//        // register watcher
//        watcher = new PathChildrenCache(client, zkAddr, true);
//    }
//
//    @Test
//    public void test() throws Exception {
//        watcher.getListenable().addListener(
//                (client1, event) -> {
//                    ChildData data = event.getData();
//                    if (data == null) {
//                        System.out.println("No data in event[" + event + "]");
//                    } else {
//                        System.out.println("Receive event: "
//                                + "type=[" + event.getType() + "]"
//                                + ", path=[" + data.getPath() + "]"
//                                + ", data=[" + new String(data.getData()) + "]"
//                                + ", stat=[" + data.getStat() + "]");
//                    }
//                });
//        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
//        System.out.println("Register zk watcher success");
//        Thread.sleep(Integer.MAX_VALUE);
//    }
}



