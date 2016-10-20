/**
 * CommandExecutor -- Praktikum Experimentierkasten -- SW3 Dieses Interface muss
 * von jedem Server implementiert werden.
 *
 * @author E. Mumprecht
 * @version 1.0 -- Geruest fuer irgendeinen Server
 * @version 1.1 -- K. Rege Fehlerr�ckgabe hinzugefuegt
 */
package ch.zhaw.ads;

public interface CommandExecutor {

    /**
     * execute -- nimmt eine Kommandozeile, tut irgendetwas gescheites, und
     * berichtet das Resultat.
     *
     * @param command Kommandozeile, �blicherweise Kommandowort gefolgt von
     * Argumenten
     * @return Resultat, �blicherweise eine oder mehrere Zeilen.
     */
	 String execute(String command) throws Exception;
}//interface CommandExecutor