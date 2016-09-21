/**
 * ServerFactory -- Praktikum Experimentierkasten --
 *
 * @author K. Rege
 * @version 1.0 -- Factory zur Erstellung von Server Objekten
 */
package ch.zhaw.ads;

public class ServerFactory {

    public CommandExecutor createServer(String name) throws Exception {
        return (CommandExecutor) Class.forName("ch.zhaw.ads." + name.substring(0, name.indexOf('.'))).newInstance();
    }
}
