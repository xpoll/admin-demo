
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class HostAddress {

	public static void main(String[] args) {
        System.out.println(getHostAddress());
    }
    
    /**
     * 获取本机IP; 由外至内返回
     */
    public static String getHostAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

					System.out.println(inetAddr.getHostAddress());
					System.out.println("isAnyLocalAddress" + ": " + inetAddr.isAnyLocalAddress());
					System.out.println("isLinkLocalAddress" + ": " + inetAddr.isLinkLocalAddress());
					System.out.println("isLoopbackAddress" + ": " + inetAddr.isLoopbackAddress());
					System.out.println("isSiteLocalAddress" + ": " + inetAddr.isSiteLocalAddress());
					System.out.println("isMCGlobal" + ": " + inetAddr.isMCGlobal());
					System.out.println("isMCLinkLocal" + ": " + inetAddr.isMCLinkLocal());
					System.out.println("isMCNodeLocal" + ": " + inetAddr.isMCNodeLocal());
					System.out.println("isMCOrgLocal" + ": " + inetAddr.isMCOrgLocal());
					System.out.println("isMCSiteLocal" + ": " + inetAddr.isMCSiteLocal());
					System.out.println("isMulticastAddress" + ": " + inetAddr.isMulticastAddress());


                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            //return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
    
}
