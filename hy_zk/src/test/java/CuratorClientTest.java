import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author: mounthuang
 * Data: 2017/5/9.
 */
//@SpringBootTest
public class CuratorClientTest {
    private static final String zkAddr = "127.0.0.1:2181";
    private static final String zkPath = "/zkTest";

    CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddr, new RetryNTimes(10, 5000));

    @Before
    public void before() {
        client.start();
        System.out.println("zk client start success");
    }

    @Test
    public void create() throws Exception {
        String data = "test zookeeper";
        System.out.println("create " + zkPath + data);
        client.create().creatingParentsIfNeeded().forPath(zkPath, data.getBytes());
    }

    @Test
    public void get() throws Exception {
        System.out.println(new String(client.getData().forPath(zkPath)));
    }

    @Test
    public void modify() throws Exception {
        String data = "modify zookeeper";
        client.setData().forPath(zkPath, data.getBytes());
        System.out.println(new String(client.getData().forPath(zkPath)));
    }

    @Test
    public void delete() throws Exception {
        client.delete().forPath(zkPath);
        System.out.println(new String(client.getData().forPath(zkPath)));
    }

    @Test
    public void testGetIp() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println(ip);
    }
}
