package totalpos;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;
import net.sf.jasperreports.engine.JRDataSource;
import srvSap.ZFISDATAFISCAL;

/**
 *
 * @author Saúl Hidalgo
 */
public class ConnectionDrivers {

    protected static ComboPooledDataSource cpds ;
    protected static ComboPooledDataSource cpdsMirror; // Just to mirror
    protected static ComboPooledDataSource cpdsProfit = null; // Connection Profit
    protected static boolean mirrorConnected = false;

    /** Crea la piscina de conexiones.
     * 
     * @return Retorna un valor verdadero en caso de que
     * se inicialice correctamente la piscina de conexiones.
     */
    protected static boolean initialize(){
        try {
            cpds = new ComboPooledDataSource();
            cpds.setCheckoutTimeout(Constants.dbTimeout);
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            String sT;
            if ( Shared.storeIp != null ){
                sT = "jdbc:mysql://" + Shared.storeIp + "/" +
                            Constants.dbName;
            }else{
                sT = "jdbc:mysql://" + Shared.getFileConfig("Server") + "/" +
                            Constants.dbName;
            }
            cpds.setJdbcUrl(sT);
            cpds.setUser(Constants.dbUser);
            cpds.setPassword(Constants.dbPassword);

            return true;

        } catch (PropertyVetoException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "No se encontró el driver.",ex);
            msb.show(Main.splash);
            return false;
        }
        
    }

    protected static boolean initializeProfit(){
        try {
            cpdsProfit = new ComboPooledDataSource();
            cpdsProfit.setCheckoutTimeout(Constants.dbTimeout);
            cpdsProfit.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + Constants.serverSQLServerProfitAdd + ":" + Constants.portSqlServer + ";DatabaseName=" + Constants.dbProfitName;
            cpdsProfit.setJdbcUrl(url);
            cpdsProfit.setUser(Constants.dbSqlServerUser);
            cpdsProfit.setPassword(Constants.dbSqlServerPassword);

            return true;

        } catch (PropertyVetoException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "No se encontró el driver.",ex);
            msb.show(Main.splash);
            return false;
        }

    }

    protected static boolean isConnected2Profit(){
        return cpdsProfit != null;
    }

    protected static boolean reinitializeOffline(){
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Shared.getFileConfig("ServerMirror") + "/" +
                            Constants.mirrorDbName;
            cpds.setJdbcUrl(sT);
            cpds.setUser(Constants.mirrordbUser);
            cpds.setPassword(Constants.mirrordbPassword);

            return true;

        } catch (PropertyVetoException ex) {
            MessageBox msb = new MessageBox(MessageBox.SGN_WARNING, "No se encontró el driver.",ex);
            msb.show(Main.splash);
            return false;
        }

    }

    /** Indica si una contraseña y usuario con correctos.
     *
     * @param user Usuario
     * @param password Contraseña
     * @throws SQLException Para problemas con la base de datos.
     * @throws Exception Para contraseña incorrecta.
     */
    protected static void login(String user, char[] password) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? and password = ? ");
        pstmt.setString(1, user.toLowerCase());
        pstmt.setString(2, Shared.hashPassword(new String(password)));
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            c.close();
            rs.close();
            throw new Exception(Constants.wrongPasswordMsg);
        }
        c.close();
        rs.close();
    }

    /** Genera la lista de usuarios.
     *
     * @return Lista de usuarios
     * @throws SQLException Para problemas de conexión con la base de datos.
     * @throws Exception
     */
    protected static List<User> listUsers() throws SQLException, Exception{
        List<User> ans = new LinkedList<User>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select login, password, nombre, apellido, tipo_de_usuario_id, bloqueado,debeCambiarPassword,puedeCambiarPassword from usuario");

        while ( rs.next() ){
            ans.add(new User(rs.getString("login").toLowerCase(),
                    rs.getString("password"),
                    rs.getString("tipo_de_usuario_id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getInt("bloqueado"),
                    rs.getInt("debeCambiarPassword"),
                    rs.getInt("puedeCambiarPassword")));
        }

        c.close();
        rs.close();
        return ans;
    }

    /** Dado un nodo, devuelve su predecesor.
     * 
     * @param e Identificador del nodo.
     * @return Predecesor.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static Edge getPredecesor(String e) throws SQLException, Exception{

        if ( e == null ) {
            return null;
        }
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id, nombre, predecesor, icono, funcion from nodo where id = ? ");
        stmt.setString(1, e);
        
        ResultSet rs = stmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return null;
        }

        Edge ee = new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion"));

        c.close();
        rs.close();

        return ee;
    }

    /**
     * Crea un perfil.
     * @param id Identificador del perfil.
     * @param description Descripción del perfil
     * @throws SQLException Para problemas de conexión con la base de datos.
     * @throws Exception 
     */
    protected static void createProfile(String id, String description) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into tipo_de_usuario( id, descripcion ) values ( ? , ? )");
        stmt.setString(1, id);
        stmt.setString(2, description);
        stmt.executeUpdate();
        c.close();
    }

    /**
     * Devuelve una lista con todos los perfiles que coincidan con ID.
     * @param id Identificador del perfil
     * @return Lista de perfiles.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Profile> listProfile(String id) throws SQLException, Exception{
        List<Profile> ans = new LinkedList<Profile>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion from tipo_de_usuario where id like ? ");
        stmt.setString(1, "%" + id + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Profile(rs.getString("id"), rs.getString("descripcion")));
        }

        c.close();
        rs.close();

        return ans;
    }

    /**
     * Dado un predecesor y un perfil, indica la lista de los sucesores en
     * los ese perfil tiene permisos para usarlo.
     * @param parent Predecesor.
     * @param profile Perfil.
     * @return Lista de sucesores.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Edge> listEdgesAllowed(String parent, String profile) throws SQLException, Exception{
        List<Edge> ans = new LinkedList<Edge>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id,n.nombre,n.predecesor,n.icono,n.funcion "
                + "from nodo n,tipo_de_usuario_puede t "
                + "where n.predecesor= ? and t.id_tipo_usuario= ? and t.id_nodo=n.id  and " + ((Constants.isPos)?"punto_de_venta = 1":"administrativo = 1"));
        stmt.setString(1, parent);
        stmt.setString(2, profile);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion")));
        }

        c.close();
        rs.close();
        return ans;
    }

    /**
     * Dado un predecesor, devuelve a todos los sucesores.
     * @param parent Predecesor.
     * @return Lista de Sucesores.
     * @throws SQLException Para problemas de conexión con la base de datos.
     */
    protected static List<Edge> listEdges(String parent) throws SQLException, Exception{
        List<Edge> ans = new LinkedList<Edge>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id,n.nombre,n.predecesor,n.icono,n.funcion "
                + "from nodo n "
                + "where n.predecesor = ? " /*and " + ((Constants.isPos)?"punto_de_venta = 1":"administrativo = 1")*/);
        stmt.setString(1, parent);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Edge(rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("predecesor"),
                    rs.getString("icono"),
                    rs.getString("funcion")));
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static boolean isAllowed(String profile, String id) throws SQLException{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select n.id "
                + "from nodo n, tipo_de_usuario_puede t "
                + "where n.id = ? and t.id_tipo_usuario = ? and t.id_nodo = n.id ");
        stmt.setString(1, id);
        stmt.setString(2, profile);

        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();
        
        c.close();
        rs.close();
        
        return ans;
    }

    protected static void disableMenuProfile(String profile, String id) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario_puede where id_tipo_usuario = ? and id_nodo = ?");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void disableInitialStock(Connection c) throws SQLException, Exception{
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario_puede where id_nodo = ?");
        stmt.setString(1, "initialStock");
        stmt.executeUpdate();
    }

    protected static void enableMenuProfile(String profile, String id) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into tipo_de_usuario_puede(id_tipo_usuario , id_nodo) values ( ? , ? )");
        stmt.setString(1, profile);
        stmt.setString(2, id);
        stmt.executeUpdate();

        c.close();
    }

    protected static void setPassword(String user, String password) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set password = ? where login = ? ");
        stmt.setString(1, password);
        stmt.setString(2, user);
        stmt.executeUpdate();

        c.close();
    }

    protected static void createUser(String username, String role, String password) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into usuario"
                + " ( login , password , tipo_de_usuario_id , bloqueado , nombre ) values ( ? , ? , ? , ? , ?)");
        stmt.setString(1, username.toLowerCase());
        stmt.setString(2, Shared.hashPassword(password));
        stmt.setString(3, role);
        stmt.setInt(4, 0);
        stmt.setString(5, username);
        stmt.executeUpdate();

        c.close();
    }

    protected static void changeProperties(String loginT, String nombreT,
            String apellidoT, String roleT,
            boolean bloqueado, boolean puede , boolean debe) throws SQLException, Exception {
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set nombre = ? ,"
                + " apellido = ? , tipo_de_usuario_id = ? , bloqueado = ? , debeCambiarPassword = ? ,"
                + " puedeCambiarPassword = ? "
                + "where login = ? ");
        stmt.setString(1, nombreT);
        stmt.setString(2, apellidoT);
        stmt.setString(3, roleT);
        stmt.setInt(4, bloqueado?1:0);
        stmt.setInt(5, debe?1:0);
        stmt.setInt(6, puede?1:0);
        stmt.setString(7, loginT);
        stmt.executeUpdate();

        c.close();
    }

    protected static boolean existsUser(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? ");
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return false;
        }

        c.close();
        rs.close();
        return true;
    }

    protected static boolean isLocked(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from usuario where login = ? and bloqueado = 1 ");
        pstmt.setString(1, username.toLowerCase());
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            c.close();
            rs.close();
            return false;
        }

        c.close();
        rs.close();
        return true;
    }

    protected static void lockUser(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set bloqueado = 1 where login = ? ");
        stmt.setString(1, username.toLowerCase());
        stmt.executeUpdate();
        c.close();
    }

    static void changeProfileDetails(String prevId, String id, String description) throws SQLException, Exception {
        if ( prevId.equals(id) ){ //Caso trivial xD;
            Connection c = ConnectionDrivers.cpds.getConnection();
            PreparedStatement stmt = c.prepareStatement("update tipo_de_usuario set descripcion = ? where id = ? ");
            stmt.setString(1, description);
            stmt.setString(2, id);
            stmt.executeUpdate();
            c.close();
            return; 
        }

        createProfile(id, description);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update tipo_de_usuario_puede set id_tipo_usuario = ? where id_tipo_usuario = ? ");
        stmt.setString(1, id);
        stmt.setString(2, prevId);
        stmt.executeUpdate();

        stmt = c.prepareStatement("update usuario set tipo_de_usuario_id = ? where tipo_de_usuario_id = ? ");
        stmt.setString(1, id);
        stmt.setString(2, prevId);
        stmt.executeUpdate();

        deleteProfile(prevId);

        c.close();
    }

    private static void deleteProfile(String id) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from tipo_de_usuario where id = ? ");
        stmt.setString(1, id);
        stmt.executeUpdate();
        c.close();
    }

    protected static void mustntChangePassword(String username) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update usuario set debeCambiarPassword = 0 where login = ? ");
        stmt.setString(1, username.toLowerCase());
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void initializeConfig() throws SQLException{
        Shared.getConfig().clear();
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select `Key` , `Value` from configuracion");
        while (rs.next()) {
            Shared.getConfig().put(rs.getString("Key"), rs.getString("Value"));
        }
        c.close();
        rs.close();

        // backward compatible =D
        if ( !Shared.getConfig().containsKey("minimunMoney") ){
            Shared.getConfig().put("minimunMoney", ".0");
        }

        if ( Shared.getConfig("holidays") != null ){
            Shared.holidays.addAll(Arrays.asList(Shared.getConfig("holidays").split(",")));
        }
    }

    protected static void saveConfig(String k, String v) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from configuracion where `key` = ? ");
        stmt.setString(1, k);
        stmt.executeUpdate();

        stmt = c.prepareStatement("insert into configuracion (`key`,`value`) values (?,?) ");
        stmt.setString(1, k);
        stmt.setString(2, v);
        stmt.executeUpdate();
        c.close();
    }

    private static List<Cost> listCosts(String code) throws SQLException{
        List<Cost> ans = new LinkedList<Cost>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select monto , fecha from costo where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(new Cost(rs.getDate("fecha"), rs.getDouble("monto")));
        }
        c.close();
        rs.close();

        return ans;
    }

    private static List<Price> listPrices(String code) throws SQLException{
        List<Price> ans = new LinkedList<Price>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select monto , fecha from precio where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(new Price(rs.getDate("fecha")
                    , (rs.getDouble("monto"))));
        }
        c.close();
        rs.close();

        return ans;
    }

    private static List<String> listBarcodes(String code) throws SQLException{
        List<String> ans = new LinkedList<String>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement( "select codigo_de_barras from codigo_de_barras where codigo_de_articulo = ? " );
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ans.add(rs.getString("codigo_de_barras"));
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Item> listItems(String barCode, String code, String description, String model) throws SQLException, Exception{
        List<Item> ans = new LinkedList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a "
                + "where a.codigo like ? and a.descripcion like ? and a.modelo like ? and "
                + "((exists  (select * from codigo_de_barras where codigo_de_barras.codigo_de_articulo = a.codigo "
                + "and codigo_de_barras.codigo_de_barras like ? "
                + ") ) or a.codigo_de_barras like ? ) limit 100");
        stmt.setString(1, "%" + code + "%");
        stmt.setString(2, "%" + description + "%");
        stmt.setString(3, "%" + model + "%");
        stmt.setString(4, "%" + barCode + "%");
        stmt.setString(5, "%" + barCode + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Item(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_registro"),
                        rs.getString("marca"),
                        rs.getString("sector"),
                        rs.getString("codigo_sublinea"),
                        rs.getString("codigo_de_barras"),
                        rs.getString("modelo"),
                        rs.getString("unidad_venta"),
                        rs.getString("unidad_compra"),
                        rs.getInt("existencia_actual"),
                        listPrices(rs.getString("codigo")),
                        listCosts(rs.getString("codigo")),
                        listBarcodes(rs.getString("codigo")),
                        rs.getBoolean("bloqueado"),
                        rs.getString("imagen"),
                        rs.getString("descuento")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Item> listItemsByModel(String model) throws SQLException{
        List<Item> ans = new LinkedList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a "
                + "where a.modelo like ? limit 100");
        stmt.setString(1, "%" + model + "%");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Item(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_registro"),
                        rs.getString("marca"),
                        rs.getString("sector"),
                        rs.getString("codigo_sublinea"),
                        rs.getString("codigo_de_barras"),
                        rs.getString("modelo"),
                        rs.getString("unidad_venta"),
                        rs.getString("unidad_compra"),
                        rs.getInt("existencia_actual"),
                        listPrices(rs.getString("codigo")),
                        listCosts(rs.getString("codigo")),
                        listBarcodes(rs.getString("codigo")),
                        rs.getBoolean("bloqueado"),
                        rs.getString("imagen"),
                        rs.getString("descuento")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Item> listFastItems(String barCode) throws SQLException{
        List<Item> ans = new LinkedList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a "
                + "where "
                + "( a.codigo_de_barras = ? or (exists  (select * from codigo_de_barras where codigo_de_barras.codigo_de_articulo = a.codigo "
                + "and codigo_de_barras.codigo_de_barras = ? "
                + ") ) ) limit 100");
        stmt.setString(1, barCode );
        stmt.setString(2, barCode );
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Item(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_registro"),
                        rs.getString("marca"),
                        rs.getString("sector"),
                        rs.getString("codigo_sublinea"),
                        rs.getString("codigo_de_barras"),
                        rs.getString("modelo"),
                        rs.getString("unidad_venta"),
                        rs.getString("unidad_compra"),
                        rs.getInt("existencia_actual"),
                        listPrices(rs.getString("codigo")),
                        listCosts(rs.getString("codigo")),
                        listBarcodes(rs.getString("codigo")),
                        rs.getBoolean("bloqueado"),
                        rs.getString("imagen"),
                        rs.getString("descuento")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static String getIdProfile(String name) throws SQLException, Exception{

        if ( name.equals("/") ) {
            return "root";
        }

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id from nodo where nombre = ? ");
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        String ans = rs.getString("id");
        c.close();
        rs.close();
        return ans;
    }

    protected static List<User> listRetailUsers() throws SQLException, Exception{
        List<User> u = listUsers();
        List<User> ans = new ArrayList<User>();

        for (User us : u) {
            if (isAllowed(us.getPerfil(), "retail")) {
                ans.add(us);
            }
        }

        return ans;
    }

    protected static List<PointOfSale> listPOS() throws SQLException{

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select identificador, descripcion, impresora, habilitada from punto_de_venta");

        List<PointOfSale> ans = new ArrayList<PointOfSale>();
        while ( rs.next() ) {
            ans.add( new PointOfSale(
                    rs.getString("identificador"),
                    rs.getString("descripcion"),
                    rs.getString("impresora"),
                    rs.getBoolean("habilitada")) );
        }
        c.close();
        rs.close();

        return ans;
    }

    /**
     * Deprecated
     * Now turns are associated to pos.
     * @param username
     * @return
     * @throws SQLException
     */
    private static boolean existTurnOpenFor(String username) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(" select * from turno "
                + "where datediff(now(),fecha) = 0 and codigo_de_usuario = ? and estado = 'Abierto'");
        stmt.setString(1, username);

        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();
        c.close();
        rs.close();

        return ans;
    }

    protected static void createTurn(String id, String description, Time a, Time b) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into turno"
                + " ( identificador , nombre , inicio, fin ) "
                + "values ( ? , ? , ? , ? )");
        stmt.setString(1, id);
        stmt.setString(2, description);
        stmt.setTime(3, a);
        stmt.setTime(4, b);
        stmt.executeUpdate();

        c.close();
    }

    protected static void modifyTurn(String id, String description, Time a, Time b) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update turno set "
                + " nombre = ? , inicio = ? , fin = ? where Identificador = ?");
        stmt.setString(1, description);
        stmt.setTime(2, a);
        stmt.setTime(3, b);
        stmt.setString(4, id);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Turn> listTurns() throws SQLException{
        List<Turn> ans = new ArrayList<Turn>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "select identificador , nombre , inicio, fin from turno ");

        while ( rs.next() ) {
            ans.add(
                    new Turn(
                        rs.getString("identificador"),
                        rs.getString("nombre"),
                        rs.getTime("inicio"),
                        rs.getTime("fin"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    private static void changeItemStock( String id, int quant ) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update articulo set existencia_actual = existencia_actual +" + quant + " where codigo = ? ");
        stmt.setString(1, id);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createReceipt(String id, String user , Assign assign) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura"
                + " ( " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") +
                ", estado, fecha_creacion , total_sin_iva , total_con_iva , iva, codigo_de_usuario, "
                + "cantidad_de_articulos , codigo_de_cliente , identificador_turno , identificador_pos) "
                + "values ( ? , 'Pedido' , now() , 0, 0, 0 , ? , 0 , \"Contado\", ? , ?)");
        stmt.setString(1, id);
        stmt.setString(2, user);
        stmt.setString(3, assign.getTurn());
        stmt.setString(4, assign.getPos());
        stmt.executeUpdate();

        c.close();
    }

    protected static double accumulatedInReceipt(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select total_sin_iva from factura where "
                + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ?");
        stmt.setString(1, receiptId);
        ResultSet rs = stmt.executeQuery();

        Double ans = null;
        boolean ok = rs.next();
        assert(ok);
        ans = rs.getDouble("total_sin_iva");
        c.close();
        rs.close();

        return ans;
    }

    protected static void addItem2Receipt(String receiptId, Item item, int quant) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into factura_contiene"
                + " ( codigo_interno_factura, codigo_de_articulo, cantidad, precio_venta, descuento) "
                + "values ( ? , ? , ? , ? , ?)");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getCode());
        stmt.setInt(3, quant);
        stmt.setDouble(4, item.getLastPrice().getQuant());
        stmt.setDouble(5, item.getDescuento());
        stmt.executeUpdate();

        changeItemStock(item.getCode(), -1*quant);

        double withoutTax = item.getLastPrice().getQuant()*quant*(100.0-item.getDescuento())/100.0;
        double subT = accumulatedInReceipt(receiptId) + withoutTax;
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = " + (subT) +
                " , total_con_iva =" + (new Price(null,subT)).plusIva().getQuant() +
                " , iva = " + (new Price(null,subT)).getIva().getQuant() +
                " , cantidad_de_articulos = cantidad_de_articulos + 1 "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();
        
        c.close();

    }

    protected static void deleteAllBufferBank(String day) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from pagos_punto_de_venta_banco where datediff(? , fecha)=0");
        stmt.setString(1, day);
        stmt.executeUpdate();
        c.close();
    }

    protected static void deleteItem2Receipt(String receiptId, Item item, int quant) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from factura_contiene where"
                + " codigo_interno_factura = ? and codigo_de_articulo = ? and cantidad = ? limit 1");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getCode());
        stmt.setInt(3, quant);
        stmt.executeUpdate();

        changeItemStock(item.getCode(), 1*quant);

        double withoutTax = -1*(item.getLastPrice().withDiscount(item.getDescuento()).getQuant())*quant;
        double subT = accumulatedInReceipt(receiptId) - withoutTax;
        stmt = c.prepareStatement("update factura "
                + "set total_sin_iva = total_sin_iva + ? " +
                " , cantidad_de_articulos = cantidad_de_articulos - 1 "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setDouble(1, withoutTax);
        stmt.setString(2, receiptId);
        stmt.executeUpdate();

        stmt = c.prepareStatement("update factura "
                + "set total_con_iva = total_sin_iva * ? " +
                " , iva = total_sin_iva * ? " +
                " , cantidad_de_articulos = cantidad_de_articulos - 1 "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setDouble(1, Double.parseDouble(Shared.getConfig("iva"))+1.0);
        stmt.setDouble(2, Double.parseDouble(Shared.getConfig("iva")));
        stmt.setString(3, receiptId);
        stmt.executeUpdate();

        c.close();

    }

    protected static int lastReceiptToday() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select count(*) from factura "
                + "where datediff(now(),fecha_creacion) = 0 and identificador_pos = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static int lastReceipt() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select count(*) from factura where identificador_pos= ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static Date getDate() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select now()");

        boolean ok = rs.next();
        assert(ok);
        Date ans = rs.getDate(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static void createPos(String number, String local, String printer) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into punto_de_venta"
                + " ( identificador, descripcion, impresora , habilitada) "
                + "values ( ? , ? , ? , 1)");
        stmt.setString(1, number);
        stmt.setString(2, local);
        stmt.setString(3, printer);
        stmt.executeUpdate();

        c.close();
    }

    protected static void modifyPos(String number, String local, String printer) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set "
                + " descripcion = ? , impresora = ? where identificador = ? ");
        stmt.setString(1, local);
        stmt.setString(2, printer);
        stmt.setString(3, number);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Assign> listAssignsTurnPosToday() throws SQLException{
        List<Assign> ans = new ArrayList<Assign>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador_turno , identificador_pos ,"
                + "a.fecha, abierto from asigna a, dia_operativo do where datediff(a.fecha,now())=0 "
                + "and codigo_punto_de_venta=identificador_pos and datediff(do.fecha,now())=0 and "
                + "do.reporteZ=0 union select identificador_turno , identificador_pos , fecha, abierto"
                + " from asigna where identificador_pos not in (select codigo_punto_de_venta from "
                + "dia_operativo where datediff(fecha,curdate())=0) and datediff(fecha,now()) = 0");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new Assign(
                        rs.getString("identificador_turno"),
                        rs.getString("identificador_pos"),
                        rs.getDate("fecha"),
                        rs.getBoolean("abierto")
                       )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<PointOfSale> listPointOfSales(boolean enabled) throws SQLException{
        List<PointOfSale> ans = new ArrayList<PointOfSale>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador , descripcion , impresora , habilitada "
                + "from punto_de_venta where habilitada = ? ");
        stmt.setBoolean(1, enabled);

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new PointOfSale(
                        rs.getString("identificador"),
                        rs.getString("descripcion"),
                        rs.getString("impresora"),
                        rs.getBoolean("habilitada"))
                        );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<PointOfSale> listPointOfSales4Assignments(boolean enabled) throws SQLException{
        List<PointOfSale> ans = new ArrayList<PointOfSale>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador, descripcion, impresora, habilitada from punto_de_venta where habilitada = ? "
                + " and identificador not in ( select codigo_punto_de_venta from dia_operativo where datediff( curdate() , fecha ) = 0 and reporteZ = 1 ) ");
        stmt.setBoolean(1, enabled);

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new PointOfSale(
                        rs.getString("identificador"),
                        rs.getString("descripcion"),
                        rs.getString("impresora"),
                        rs.getBoolean("habilitada"))
                        );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static void createAssign(Assign a) throws SQLException{

        if ( ! assignIsOk(a) ){
            throw new SQLException(Constants.duplicatedMsg);
        }

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into asigna "
                + "(identificador_turno , identificador_pos , fecha , "
                + "abierto )  values ( ? , ? , now() , ? )");
        stmt.setString(1, a.getTurn());
        stmt.setString(2, a.getPos());
        stmt.setBoolean(3, a.isOpen());
        stmt.executeUpdate();

        c.close();
    }

    private static boolean isOverlapping(Assign a) throws SQLException{
        List<Assign> l = listAssignsTurnPosToday();
        Turn newTurn = Shared.getTurn(listTurns(), a.getTurn());
        for (Assign assign : l) {
            Turn t = Shared.getTurn(listTurns(), assign.getTurn());
            // Yo denominaría a esto como un if en Cascada :-o!
            if ( a.getPos().equals(assign.getPos()) &&
                    (a.getTurn().equals(assign.getTurn()) ||
                        ( (t.getInicio().before(newTurn.getFin()) && newTurn.getInicio().before(t.getFin())) ||
                            (newTurn.getInicio().before(t.getFin()) && t.getInicio().before(newTurn.getFin())) ) )){
                return true;
            }
        }
        return false;
    }

    private static boolean isExpired(Assign a) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Turn t = Shared.getTurn(listTurns(), a.getTurn());

        PreparedStatement stmt = c.prepareStatement("select curtime()");
        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();
        assert(ans);
        ans = rs.getTime(1).before(t.getFin());

        c.close();
        return !ans;
    }

    private static boolean assignIsOk(Assign a) throws SQLException{
        return !isOverlapping(a) && !isExpired(a);
    }

    protected static List<Assign> listAssignsTurnPosRightNow() throws SQLException{
        List<Assign> ans = new ArrayList<Assign>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.identificador_turno, "
                + "a.identificador_pos , a.fecha , a.abierto "
                + "from asigna a , turno t "
                + "where datediff(fecha,now()) = 0 and "
                + "t.Identificador = a.identificador_turno and t.inicio <= now() and t.fin >= now()");

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ) {
            ans.add(
                    new Assign(
                        rs.getString("identificador_turno"),
                        rs.getString("identificador_pos"),
                        rs.getDate("fecha"),
                        rs.getBoolean("abierto"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static void putToIdle(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Espera' where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    protected static void putToNormal(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Pedido' where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }


    protected static void cancelReceipt(String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Anulada' where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();

        c.close();
    }

    protected static List<Item> listItems(String receiptID) throws SQLException{
        List<Item> ans = new ArrayList<Item>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento "
                + "from articulo a , factura_contiene fc where fc.codigo_interno_factura = ? and fc.codigo_de_articulo = a.codigo");
        stmt.setString(1, receiptID);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Item(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_registro"),
                        rs.getString("marca"),
                        rs.getString("sector"),
                        rs.getString("codigo_sublinea"),
                        rs.getString("codigo_de_barras"),
                        rs.getString("modelo"),
                        rs.getString("unidad_venta"),
                        rs.getString("unidad_compra"),
                        rs.getInt("existencia_actual"),
                        listPrices(rs.getString("codigo")),
                        listCosts(rs.getString("codigo")),
                        listBarcodes(rs.getString("codigo")),
                        rs.getBoolean("bloqueado"),
                        rs.getString("imagen"),
                        rs.getString("descuento")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Item2Receipt> listItems2CN(String CNId) throws SQLException{
        List<Item2Receipt> ans = new ArrayList<Item2Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento , fc.cantidad , fc.devuelto, fc.precio_venta , fc.descuento "
                + "from articulo a , nota_de_credito_contiene fc where fc.codigo_interno_nota_de_credito = ? and fc.codigo_de_articulo = a.codigo");
        stmt.setString(1, CNId);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Item2Receipt(
                        new Item(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_registro"),
                            rs.getString("marca"),
                            rs.getString("sector"),
                            rs.getString("codigo_sublinea"),
                            rs.getString("codigo_de_barras"),
                            rs.getString("modelo"),
                            rs.getString("unidad_venta"),
                            rs.getString("unidad_compra"),
                            rs.getInt("existencia_actual"),
                            listPrices(rs.getString("codigo")),
                            listCosts(rs.getString("codigo")),
                            listBarcodes(rs.getString("codigo")),
                            rs.getBoolean("bloqueado"),
                            rs.getString("imagen"),
                            rs.getString("descuento")
                        ),
                        rs.getInt("cantidad"),
                        rs.getInt("devuelto"),
                        rs.getDouble("fc.precio_venta"),
                        rs.getDouble("fc.descuento"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Item2Receipt> listItems2Receipt(String receiptID) throws SQLException{
        List<Item2Receipt> ans = new ArrayList<Item2Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select a.codigo, a.descripcion, a.fecha_registro, a.marca, a.sector,"
                + " a.codigo_sublinea , a.codigo_de_barras , a.modelo , a.unidad_venta , a.unidad_compra , a.existencia_actual , a.bloqueado , a.imagen , a.descuento , fc.cantidad , fc.devuelto, fc.precio_venta , fc.descuento "
                + "from articulo a , factura_contiene fc where fc.codigo_interno_factura = ? and fc.codigo_de_articulo = a.codigo");
        stmt.setString(1, receiptID);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Item2Receipt(
                        new Item(
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_registro"),
                            rs.getString("marca"),
                            rs.getString("sector"),
                            rs.getString("codigo_sublinea"),
                            rs.getString("codigo_de_barras"),
                            rs.getString("modelo"),
                            rs.getString("unidad_venta"),
                            rs.getString("unidad_compra"),
                            rs.getInt("existencia_actual"),
                            listPrices(rs.getString("codigo")),
                            listCosts(rs.getString("codigo")),
                            listBarcodes(rs.getString("codigo")),
                            rs.getBoolean("bloqueado"),
                            rs.getString("imagen"),
                            rs.getString("descuento")
                        ),
                        rs.getInt("cantidad"),
                        rs.getInt("devuelto"),
                        rs.getDouble("fc.precio_venta"),
                        rs.getDouble("fc.descuento"))
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listIdleReceiptToday() throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno , codigo_interno_alternativo "
                + "from factura where estado='Espera' and datediff(fecha_creacion,now()) = 0 and identificador_pos = ? ");

        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno"),
                            rs.getString("codigo_interno_alternativo")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listThisReceipt(String internal_code) throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno , codigo_interno_alternativo "
                + "from factura where estado='Facturada' and identificador_pos = ? and codigo_interno = ? ");

        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.setString(2, internal_code);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno"),
                            rs.getString("codigo_interno_alternativo")
                        )
                    );
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listUncompletedReceiptToday() throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno , codigo_interno_alternativo "
                + "from factura where estado='Pedido' and datediff(fecha_creacion,now()) = 0 and identificador_pos = ? ");

        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Receipt r = new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(Shared.isOffline?rs.getString("codigo_interno_alternativo"):rs.getString("codigo_interno")),
                            rs.getString("identificador_turno"),
                            rs.getString("codigo_interno_alternativo")
                        );
            if ( !r.getItems().isEmpty() ){
                ans.add(r);
            }
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static void cancelAllReceipts() throws SQLException{
        // for performace;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(
                "update factura set estado = 'Anulada' where estado = 'Pedido' and identificador_pos = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.executeUpdate();
        c.close();
    }

    protected static String getMyPrinter() throws SQLException{
        List<PointOfSale> poses = listPointOfSales(true);
        for (PointOfSale pointOfSale : poses) {
            if ( pointOfSale.getId().equals(Shared.getFileConfig("myId")) ){
                return pointOfSale.getPrinter();
            }
        }
        return null;
    }

    protected static List<Client> listClients(String id) throws SQLException{
        List<Client> ans = new ArrayList<Client>();
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo , nombre, direccion, telefono "
                + "from cliente "
                + "where codigo = ? ");
        stmt.setString(1, id );
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Client(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static void createClient(Client client) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(
                "insert into cliente ( codigo, nombre , direccion , telefono ) values ( ? , ? , ? , ? )");
        stmt.setString(1, client.getId());
        stmt.setString(2, client.getName());
        stmt.setString(3, client.getAddress());
        stmt.setString(4, client.getPhone());
        stmt.executeUpdate();
        c.close();
    }

    protected static void modifyClient(Client myClient) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update cliente set nombre = ? , direccion = ? , telefono = ? where codigo = ?");
        stmt.setString(1, myClient.getName());
        stmt.setString(2, myClient.getAddress());
        stmt.setString(3, myClient.getPhone());
        stmt.setString(4, myClient.getId());
        stmt.executeUpdate();

        c.close();
    }

    protected static boolean wasAssignUsedToday(Assign t) throws SQLException{
        boolean ans = false;

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * "
                + "from factura where datediff(now(),fecha_creacion) = 0 and identificador_turno = ? and identificador_pos = ?");
        stmt.setString(1, t.getTurn() );
        stmt.setString(2, t.getPos() );
        ResultSet rs = stmt.executeQuery();

        ans = rs.next();

        c.close();
        rs.close();

        return ans;
    }

    protected static void deleteAssignToday(Assign t) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from asigna where datediff(now(),fecha) = 0 and identificador_turno = ? and identificador_pos = ?");
        stmt.setString(1, t.getTurn() );
        stmt.setString(2, t.getPos() );
        stmt.executeUpdate();

        c.close();
    }

    protected static void setAssignOpen(Assign a, boolean isOpen) throws SQLException, Exception {
        if ( isExpired(a) ){
            throw new Exception("No se puede modificar una asignación expirada");
        }
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update asigna set abierto = ? where identificador_turno = ? and identificador_pos = ? and datediff( fecha, ? ) = 0");
        stmt.setInt(1, isOpen?1:0);
        stmt.setString(2, a.getTurn());
        stmt.setString(3, a.getPos());
        stmt.setDate(4, a.getDate());
        stmt.executeUpdate();

        c.close();
    }

    protected static void deleteAllStores() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from almacen");
        stmt.executeUpdate();
        c.close();
    }

    protected static void createStore(DefaultTableModel model) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = (String) model.getValueAt(i, 0) ;
            String description = (String) model.getValueAt(i, 1);
            PreparedStatement stmt = c.prepareStatement(
                "insert into almacen ( codigo, descripcion ) values ( ? , ? )");
            stmt.setString(1, id);
            stmt.setString(2, description);
            stmt.executeUpdate();
        }
        
        c.close();
    }

    protected static List<Store> listStores() throws SQLException{
        List<Store> ans = new ArrayList<Store>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo , descripcion "
                + "from almacen");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Store(
                        rs.getString("codigo"),
                        rs.getString("descripcion")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static void flipEnabledPointOfSale(PointOfSale p) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set habilitada = ? where identificador = ? ");
        stmt.setInt(1, !p.isEnabled()?1:0);
        stmt.setString(2, p.getId());
        stmt.executeUpdate();

        c.close();
    }

    protected static void updateReportZ(String z) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set reporte_z = ? where identificador = ? ");
        stmt.setString(1, z);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void setFiscalData(Connection c, String actualId, String serial, String z, String fiscalNumber) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("update factura set impresora = ? , numero_fiscal = ? , numero_reporte_z = ? "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo ":"codigo_interno ") + " = ? ");
        stmt.setString(1, serial);
        stmt.setString(2, fiscalNumber);
        stmt.setString(3, z);
        stmt.setString(4, actualId);
        stmt.executeUpdate();
    }

    protected static void setFiscalDataCN(Connection c, String actualId, String serial, String z, String fiscalNumber) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("update nota_de_credito set impresora = ? , numero_fiscal = ? , numero_reporte_z = ? "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, serial);
        stmt.setString(2, fiscalNumber);
        stmt.setString(3, z);
        stmt.setString(4, actualId);
        stmt.executeUpdate();
    }

    protected static void finishReceipt(Connection c, String receiptId) throws SQLException{
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  estado = 'Facturada' where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, receiptId);
        stmt.executeUpdate();
    }

    protected static void setGlobalDiscount(String receiptId , Double d) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  descuento_global = ? where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setDouble(1, d);
        stmt.setString(2, receiptId);
        stmt.executeUpdate();
        
        c.close();
    }

    protected static void setClient(Connection c, Client cu, String actualId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("update factura set "
                + "  codigo_de_cliente = ? where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, cu.getId());
        stmt.setString(2, actualId);
        stmt.executeUpdate();
    }

    protected static void setPritingHour(Connection c, String actualId, String table) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("update " + table + " set "
                + "  fecha_impresion = now() where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(1, actualId);
        stmt.executeUpdate();
    }

    protected static boolean hasMovements() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * from factura");
        ResultSet rs = stmt.executeQuery();
        boolean ans = rs.next();

        c.close();
        return ans;
    }

    protected static void deleteAllBPos() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from punto_de_venta_de_banco");
        stmt.executeUpdate();
        c.close();
    }

    protected static void createBPOS(DefaultTableModel model) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = (String) model.getValueAt(i, 0) ;
            String description = (String) model.getValueAt(i, 1);
            String lot = (String) model.getValueAt(i, 2);
            String posId = (String) model.getValueAt(i, 3);
            String kindbpos = (String) model.getValueAt(i, 4);
            PreparedStatement stmt = c.prepareStatement(
                "insert into punto_de_venta_de_banco ( id, descripcion, lote , identificador_pos, tipo) values ( ? , ? , ? , ? , ? )");
            stmt.setString(1, id);
            stmt.setString(2, description);
            stmt.setString(3, lot);
            stmt.setString(4, posId);
            stmt.setString(5, kindbpos);
            stmt.executeUpdate();
        }

        c.close();
    }

    protected static List<BankPOS> listBPos() throws SQLException {
        List<BankPOS> ans = new ArrayList<BankPOS>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion, lote, identificador_pos , tipo from punto_de_venta_de_banco");
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new BankPOS(
                        rs.getString("id"),
                        rs.getString("descripcion"),
                        rs.getString("lote"),
                        rs.getString("identificador_pos"),
                        rs.getString("tipo")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static List<BankPOS> listBPos(String kindbpos) throws SQLException {
        List<BankPOS> ans = new ArrayList<BankPOS>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select id , descripcion, lote, identificador_pos , tipo from punto_de_venta_de_banco "
                + "where ( tipo = ?  or tipo = ? ) ");
        stmt.setString(1, kindbpos);
        stmt.setString(2, Constants.kindOfBPOS[Constants.kindOfBPOS.length-1]);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new BankPOS(
                        rs.getString("id"),
                        rs.getString("descripcion"),
                        rs.getString("lote"),
                        rs.getString("identificador_pos"),
                        rs.getString("tipo")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static void savePayForm(List<PayForm> lpf) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        for (PayForm payForm : lpf) {
            PreparedStatement stmt = c.prepareStatement(
                "insert into forma_de_pago ( fecha, codigo_interno_factura, tipo, codigo_punto_de_venta_de_banco , lote , monto, codigo_punto_de_venta) "
                + "values ( now(), ? , ? , ? , ? , ? , ?)");
            stmt.setString(1, payForm.getReceiptId());
            stmt.setString(2, payForm.getFormWay());
            stmt.setString(3, payForm.getbPos());
            stmt.setString(4, payForm.getLot());
            stmt.setDouble(5, payForm.getQuant());
            stmt.setString(6, Shared.getFileConfig("myId"));
            stmt.executeUpdate();
            if ( payForm.getFormWay().equals(Constants.cashPaymentName) ){
                addCash(payForm.getQuant(), Shared.getFileConfig("myId"));
            }else if ( payForm.getFormWay().equals(Constants.debitPaymentName) ){
                addDebit(payForm.getQuant(), Shared.getFileConfig("myId"));
            }else if ( payForm.getFormWay().equals(Constants.creditPaymentName) ){
                addCredit(payForm.getQuant(), Shared.getFileConfig("myId"));
            }else if ( payForm.getFormWay().equals(Constants.cashPaymentName) ){
                addCash(-1*payForm.getQuant(), Shared.getFileConfig("myId"));
            }else if ( payForm.getFormWay().equals(Constants.CNPaymentName) ){
                addCreditNote(payForm.getQuant(), Shared.getFileConfig("myId"));
            } else if ( payForm.getFormWay().equals(Constants.americanExpressPaymentName) ){
                // TODO CREATE TABLES FOR AMERICAN EXPRESS
            }else {
                // This should not happend
                assert(false);
            }
        }

        c.close();
    }

    protected static void renameReceipts() throws SQLException{
        List<String> tmpCode = new LinkedList<String>();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno_alternativo , identificador_pos from factura "
                + "where codigo_interno='' and identificador_pos = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            tmpCode.add(rs.getString("codigo_interno_alternativo"));
        }

        rs.close();

        for (String code : tmpCode) {
            String newCode = Shared.nextId(1);
            stmt = c.prepareStatement("update factura set codigo_interno = ? where codigo_interno_alternativo= ? ");
            stmt.setString(1, newCode);
            stmt.setString(2, code);
            stmt.executeUpdate();
            stmt = c.prepareStatement("update factura_contiene set codigo_interno_factura = ? where codigo_interno_factura = ? ");
            stmt.setString(1, newCode);
            stmt.setString(2, code);
            stmt.executeUpdate();
            stmt = c.prepareStatement("update forma_de_pago set codigo_interno_factura = ? where codigo_interno_factura = ? ");
            stmt.setString(1, newCode);
            stmt.setString(2, code);
            stmt.executeUpdate();
        }

        c.close();
    }

    protected static void renameCN() throws SQLException{
        List<String> tmpCode = new LinkedList<String>();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno_alternativo , identificador_pos from nota_de_credito "
                + "where codigo_interno='' and identificador_pos = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            tmpCode.add(rs.getString("codigo_interno_alternativo"));
        }

        rs.close();

        for (String code : tmpCode) {
            String newCode = Shared.nextIdCN(1);
            stmt = c.prepareStatement("update nota_de_credito set codigo_interno = ? where codigo_interno_alternativo = ? ");
            stmt.setString(1, newCode);
            stmt.setString(2, code);
            stmt.executeUpdate();
            stmt = c.prepareStatement("update nota_de_credito_contiene set codigo_interno_nota_de_credito = ? "
                    + "where codigo_interno_nota_de_credito = ? ");
            stmt.setString(1, newCode);
            stmt.setString(2, code);
            stmt.executeUpdate();
        }

        c.close();
    }

    protected static void createCreditNote(String myId, String idReceipt, String user, Assign assign, List<Item2Receipt> items) throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        double subT = .0 , ivaT = .0 , total = .0 ;
        for (Item2Receipt item2r : items) {
            subT += Shared.round( new Price(null,item2r.getSellPrice()).withDiscount(item2r.getSellDiscount()).getQuant(), 2 )*item2r.getQuant();
        }
        ivaT = new Price(null, subT).getIva().getQuant();
        total = subT + ivaT;

        PreparedStatement stmt = c.prepareStatement("insert into nota_de_credito"
                + " ( " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") +
                " , codigo_factura, estado, fecha_creacion , total_sin_iva , "
                + "total_con_iva , iva, codigo_de_usuario, cantidad_de_articulos , identificador_turno"
                + " , identificador_pos) "
                + "values ( ?, ? , 'Nota' , now() , ? , ?, ? , ? , ? , ? , ?)");
        stmt.setString(1, myId);
        stmt.setString(2, idReceipt);
        stmt.setDouble(3, subT);
        stmt.setDouble(4, total);
        stmt.setDouble(5, ivaT);
        stmt.setString(6, user);
        stmt.setInt(7, items.size());
        stmt.setString(8, assign.getTurn());
        stmt.setString(9, assign.getPos());
        stmt.executeUpdate();

        c.close();

        for (Item2Receipt item2r : items) {
            addItem2CreditNote(myId, item2r, getPrice(item2r.getItem().getCode(),idReceipt) , getDiscount( item2r.getItem().getCode(),idReceipt ) );
            deleteItemFromReceipt(item2r,idReceipt);
        }
    }

    protected static Double getPrice(String itemId , String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select precio_venta from factura_contiene where"
                + " codigo_interno_factura=? and codigo_de_articulo=?");
        stmt.setString(1, receiptId);
        stmt.setString(2, itemId);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static Double getDiscount(String itemId , String receiptId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select descuento from factura_contiene where"
                + " codigo_interno_factura=? and codigo_de_articulo=?");
        stmt.setString(1, receiptId);
        stmt.setString(2, itemId);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static int lastCreditNoteToday() throws SQLException, Exception{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(
                "select count(*) from nota_de_credito "
                + "where datediff(now(),fecha_creacion) = 0");

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static int lastCreditNote() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select count(*) from nota_de_credito where identificador_pos = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        int ans = rs.getInt(1);
        c.close();
        rs.close();

        return ans;
    }

    protected static Receipt getReceiptToDev(String id) throws SQLException {
        String campoId = "codigo_interno";
        String campoIdW = "codigo_interno";
        if ( id.charAt(6) == '9' ){
            campoIdW = "codigo_interno_alternativo";
            if ( Shared.isOffline ){
                campoId = "codigo_interno_alternativo";
            }
        }
        
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select " + campoId + " , estado , fecha_creacion , fecha_impresion, codigo_de_cliente , total_con_iva ,"
                + "impresora , numero_fiscal , numero_reporte_z , descuento_global, codigo_interno_alternativo "
                + " from factura where " + campoIdW + " = ? and estado='Facturada'");
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();

        Receipt ans = null;
        if ( ok ){
            ans = new Receipt(rs.getString(campoId), "Facturada",rs.getTimestamp("fecha_creacion"), rs.getTimestamp("fecha_impresion"), rs.getString("codigo_de_cliente")
                    , null, rs.getDouble("total_con_iva"), rs.getDouble("descuento_global"), null, rs.getString("impresora"),
                    rs.getString("numero_fiscal"), rs.getString("numero_reporte_z"),
                    null, null, listItems2Receipt(rs.getString(campoId)), null, rs.getString("codigo_interno_alternativo"));
        }
        c.close();
        rs.close();
        return ans;
    }

    protected static void addItem2CreditNote(String receiptId, Item2Receipt item, Double sellPrice, Double discount) throws SQLException, Exception{

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into nota_de_credito_contiene"
                + " ( codigo_interno_nota_de_credito, codigo_de_articulo , cantidad, devuelto , precio_venta , descuento) "
                + "values ( ? , ? , ? , 0 , ? , ? )");
        stmt.setString(1, receiptId);
        stmt.setString(2, item.getItem().getCode());
        stmt.setInt(3, item.getQuant());
        stmt.setDouble(4, sellPrice);
        stmt.setDouble(5, discount);
        stmt.executeUpdate();

        changeItemStock(item.getItem().getCode(), 1*item.getQuant());

        double withoutTax = item.getItem().getLastPrice().getQuant();
        double subT = .0;// = accumulatedInReceipt(receiptId) + withoutTax;
        stmt = c.prepareStatement("update nota_de_credito set " +
                /*+ "set total_sin_iva = " + (subT) +
                " , total_con_iva =" + (new Price(null,subT)).plusIva().getQuant() +
                " , iva = " + (new Price(null,subT)).getIva().getQuant() +*/
                " cantidad_de_articulos = cantidad_de_articulos + ? "
                + "where " + (Shared.isOffline?"codigo_interno_alternativo":"codigo_interno") + " = ? ");
        stmt.setString(2, receiptId);
        stmt.setInt(1, item.getQuant());
        stmt.executeUpdate();

        c.close();

    }

    protected static void changeLot( String idBpos , String newLot) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update punto_de_venta_de_banco "
                + "set lote = ? where id = ? ");
                
        stmt.setString(1, newLot);
        stmt.setString(2, idBpos);
        stmt.executeUpdate();

        c.close();
    }

    protected static Double getAllCash(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto from forma_de_pago "
                + "where tipo = 'Efectivo' and datediff(fecha,?)=0");
        stmt.setString(1, day );
        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();

        if ( !ans ){
            c.close();
            rs.close();
            return .0;
        }

        Double doubl = rs.getDouble("monto");

        c.close();
        rs.close();

        return doubl;
    }

    protected static Double getCashToday(String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select dinero_efectivo "
                + "from dia_operativo where datediff(now(),fecha) = 0 and codigo_punto_de_venta = ? ");
        stmt.setString(1, Shared.getFileConfig("myId") );
        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();

        if ( !ans ){
            c.close();
            rs.close();
            return -1.0;
        }

        Double doubl = rs.getDouble("dinero_efectivo");

        c.close();
        rs.close();

        return doubl;
    }

    protected static Double maximunMoney2Extract(String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) from "
                + "(select sum(monto) as monto from movimiento_efectivo where "
                + "datediff(curdate(),fecha)=0 and identificador_punto_de_venta = ? "
                + "and monto < 0 union select dinero_efectivo from dia_operativo where "
                + "datediff(now(),fecha)=0 and codigo_punto_de_venta = ?) as myTable");
        stmt.setString(1, pos);
        stmt.setString(2, pos);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Double ans = rs.getDouble(1);
        c.close();
        rs.close();
        return ans;
    }

    protected static void setCash(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_efectivo = ? where codigo_punto_de_venta = ? and datediff(curdate(),fecha)=0");

        stmt.setDouble(1, money);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void addCash(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_efectivo = dinero_efectivo + ? where codigo_punto_de_venta = ? and datediff(curdate(),fecha)=0 ");

        stmt.setDouble(1, money);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void addCredit(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_tarjeta_credito = dinero_tarjeta_credito + ? where codigo_punto_de_venta = ? and datediff(curdate(),fecha)=0");

        stmt.setDouble(1, money);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void addCreditNote(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set nota_de_credito = nota_de_credito + ? where codigo_punto_de_venta = ? and datediff(curdate(),fecha) = 0 ");

        stmt.setDouble(1, money);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void addDebit(Double money, String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set dinero_tarjeta_debito = dinero_tarjeta_debito + ? where codigo_punto_de_venta = ? and datediff(curdate(),fecha)=0 ");

        stmt.setDouble(1, money);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void createOperativeDay() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("insert into dia_operativo ( fecha , codigo_punto_de_venta , dinero_tarjeta_credito "
                + ", dinero_efectivo , dinero_tarjeta_debito , nota_de_credito ) values ( curdate() , ? , .0 , .0 , .0 , 0)");

        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static String getDiff(Time t) throws SQLException, ParseException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select max(t) from "
                + "(select convert(timediff(curtime(),?),char) as t union "
                + "select convert(timediff(?,curtime()),char)) as myTable");
        stmt.setTime(1, t);
        stmt.setTime(2, t);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String ansS = rs.getString(1);
        //ansS = ansS.substring(1);
        //Time ans = new Time(sdf.parse(ansS).getTime());
        c.close();
        rs.close();
        return ansS;
    }

    private static void deleteItemFromReceipt(Item2Receipt item2r, String receiptId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update factura_contiene set devuelto = devuelto + ? "
                + "where codigo_interno_factura = ? and cantidad >= devuelto + ? and  codigo_de_articulo = ? limit 1");
        stmt.setString(2, receiptId);
        stmt.setInt(1, item2r.getQuant());
        stmt.setInt(3, item2r.getQuant());
        stmt.setString(4, item2r.getItem().getCode());
        stmt.executeUpdate();
        c.close();
    }

    protected static List<Expense> listExpenses(String day) throws SQLException {
        List<Expense> ans = new ArrayList<Expense>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select concepto , monto , descripcion from gasto "
                + "where datediff( ? ,fecha) = 0 ");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Expense(
                        rs.getString("concepto"),
                        rs.getDouble("monto"),
                        rs.getString("descripcion")
                        )
                    );
        }

        c.close();
        rs.close();

        return ans;
    }

    protected static void deleteAllExpenses(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from gasto where datediff(?,fecha) = 0 ");
        stmt.setString(1, day);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createExpenses(DefaultTableModel model, String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String concept = (String) model.getValueAt(i, 0) ;
            String description = (String) model.getValueAt(i, 2) ;
            Double quant = Double.parseDouble(((String) model.getValueAt(i, 1)).replace(',', '.'));
            PreparedStatement stmt = c.prepareStatement(
                "insert into gasto ( fecha, concepto, monto , descripcion ) values ( ? , ? , ? , ? )");
            stmt.setString(1, day);
            stmt.setString(2, concept);
            stmt.setDouble(3, quant);
            stmt.setString(4, description);
            stmt.executeUpdate();
        }
        c.close();
    }

    protected static JRDataSource createDataSource(List<Parameter> parameters, String sql, List<Column> columns) throws SQLException {
        String[] columnsArray = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnsArray[i] = columns.get(i).getName();
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        for (int i = 0; i < parameters.size(); i++) {
            Parameter p = parameters.get(i);
            String[] positions = p.getPositions().split(":");
            for (String pos : positions) {
                int myI = Integer.parseInt(pos);
                if ( p.getTextField() instanceof JTextField ){
                    JTextField jtf = (JTextField) p.getTextField();
                    stmt.setString(myI, jtf.getText());
                }else if ( p.getTextField() instanceof JComboBox ){
                    JComboBox jtf = (JComboBox) p.getTextField();
                    stmt.setString(myI, (String) jtf.getSelectedItem());
                }
            }
            
        }
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[columns.size()];
            for (int i = 0; i < columnsArray.length; i++) {
                if ( columns.get(i).getMyClass().equals("bigDecimalType") ){
                    toAdd[i] = new BigDecimal(rs.getDouble(columnsArray[i]));
                }else{
                    toAdd[i] = rs.getString(columnsArray[i]);
                }
                
            }
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    protected static List<Deposit> listDeposits(String day) throws SQLException {
        List<Deposit> ans = new ArrayList<Deposit>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select banco, numero, monto from deposito "
                + "where datediff(?,fecha) = 0 ");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(
                    new Deposit(
                        rs.getString("banco"),
                        rs.getString("numero"),
                        rs.getDouble("monto")
                        )
                    );
        }
        
        c.close();
        rs.close();
        
        return ans;
    }

    protected static void deleteAllDeposits(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from deposito where datediff(?,fecha) = 0 ");
        stmt.setString(1, day);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createDeposits(DefaultTableModel model, String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String bank = (String) model.getValueAt(i, 0) ;
            Double quant = Double.parseDouble(((String) model.getValueAt(i, 2)).replace(',', '.'));
            String formId = (String) model.getValueAt(i, 1) ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into deposito ( fecha, banco, numero, monto ) values ( ? , ? , ? , ? )");
            stmt.setString(1, day);
            stmt.setString(2, bank);
            stmt.setString(3, formId);
            stmt.setDouble(4, quant);
            stmt.executeUpdate();
        }
        c.close();
    }

    protected static void listFormWayXPos(DefaultTableModel ans, String day) throws SQLException{
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select do.codigo_punto_de_venta , "
                + "do.dinero_efectivo, do.dinero_tarjeta_credito + do.dinero_tarjeta_debito as dinero_tarjeta "
                + ", do.nota_de_credito , sum(me.monto) as retiros , do.dinero_efectivo+sum(me.monto) as suma from dia_operativo do ,"
                + " movimiento_efectivo me where datediff( do.fecha , ? ) = 0 and me.identificador_punto_de_venta "
                + "= do.codigo_punto_de_venta and datediff(me.fecha,?)=0 group by codigo_punto_de_venta");

        stmt.setString(1, day);
        stmt.setString(2, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("dinero_efectivo")
                    ,rs.getString("dinero_tarjeta"),rs.getString("nota_de_credito") , rs.getString("retiros"), rs.getString("suma")};
            ans.addRow(s);
        }

        c.close();
        rs.close();

    }

    protected static void listFiscalZ(DefaultTableModel ans, String day) throws SQLException{
        ans.setRowCount(0);

        System.out.println("Day = " + day);
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select do.codigo_punto_de_venta , pos.impresora , "
                + "do.dinero_tarjeta_credito + do.dinero_efectivo+ do.dinero_tarjeta_debito as facturado, "
                + "do.total_ventas * ? as facturado_impresora ,"
                + "reporteZ"
                + " from dia_operativo as do , punto_de_venta as pos where datediff(fecha,?)=0 and do.codigo_punto_de_venta "
                + "= pos.identificador");
        stmt.setDouble(1, (Shared.getIva()/100.0+1.0));
        stmt.setString(2, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            System.out.println("Numero: " + Constants.df.format((ConnectionDrivers.getSumTotalWithIva(day,"factura","Facturada", true , rs.getString("codigo_punto_de_venta")))));
            Object[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("impresora")
                    , Constants.df.format((ConnectionDrivers.getSumTotalWithIva(day,"factura","Facturada", true , rs.getString("codigo_punto_de_venta"))
                        - ConnectionDrivers.getSumTotalWithIva(day,"nota_de_credito","Nota",false, rs.getString("codigo_punto_de_venta")))
                        *(Shared.getIva()/100.0+1.0))
                    ,Constants.df.format(rs.getDouble("facturado_impresora")), rs.getBoolean("reporteZ") };
            ans.addRow(s);
        }

        c.close();
        rs.close();
    }

    protected static void listFormWayXPosesDetail(DefaultTableModel ans, String day) throws SQLException {
        ans.setRowCount(0);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_punto_de_venta,tipo,sum(monto) "
                + "as monto from forma_de_pago where datediff(?,fecha)=0 "
                + "group by codigo_punto_de_venta , tipo");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            String[] s = {rs.getString("codigo_punto_de_venta"),rs.getString("tipo"),rs.getString("monto")};
            ans.addRow(s);
        }

        c.close();
        rs.close();
    }

    protected static boolean wereCalculatedBanks(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * from pagos_punto_de_venta_banco where datediff(?,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ans = rs.next();

        c.close();
        rs.close();
        return ans;
    }

    protected static void listBankTable(DefaultTableModel ans, String day) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        
        if ( wereCalculatedBanks(day) ){
            System.out.println("Ya fue calculado!");
            ;// Just load it!! It was calculated
        }else{
            System.out.println("Calculando...");
            PreparedStatement stmt = c.prepareStatement("select concat(concat(a.codigo_punto_de_venta_de_banco,' - ') , b.descripcion) as codigo_punto_de_venta_de_banco , a.lote,"
                + "a.tipo , sum(monto) as monto from forma_de_pago a,"
                + "punto_de_venta_de_banco b where a.codigo_punto_de_venta_de_banco = b.id and "
                + "datediff(?,fecha) = 0 group by a.codigo_punto_de_venta_de_banco , a.tipo");

            stmt.setString(1, day);
            ResultSet rs = stmt.executeQuery();

            while ( rs.next() ){
                System.out.println("Detalle");
                PreparedStatement stmt2 =  c.prepareStatement(
                        "insert into pagos_punto_de_venta_banco(fecha," +
                        "punto_de_venta_de_banco,lote,medio,declarado," +
                        "monto_real) values(?,?,?,?,?,?)");

                stmt2.setString(1, day);
                stmt2.setString(2, rs.getString("codigo_punto_de_venta_de_banco") );
                stmt2.setString(3, rs.getString("lote"));
                stmt2.setString(4, rs.getString("tipo"));
                stmt2.setString(5, rs.getString("monto"));
                stmt2.setString(6, rs.getString("monto"));
                stmt2.executeUpdate();

            }

        }
        ans.setRowCount(0);
        PreparedStatement stmt = c.prepareStatement("select punto_de_venta_de_banco "
                + ", lote, medio, declarado, monto_real from pagos_punto_de_venta_banco "
                + "where datediff(fecha, ? )=0");
        stmt.setString(1, day );

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] s = {rs.getString("punto_de_venta_de_banco") ,rs.getString("lote")
                    ,rs.getString("medio"),rs.getString("declarado"),rs.getDouble("monto_real")};
            ans.addRow(s);
        }
        c.close();
    }

    protected static Double getTotalCards(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select declarado, monto_real from pagos_punto_de_venta_banco where datediff(?,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        Double ans = .0;
        while( rs.next() ){
            ans += rs.getDouble("monto_real");
        }
        
        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalPCN(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto from forma_de_pago where tipo='Nota de Credito' "
                + "and datediff(?,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalCashfromPrinter(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(dinero_efectivo_impresora) as monto from dia_operativo "
                + "where datediff(?,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalCash(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto from deposito "
                + "where datediff(?,fecha) = 0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalCN(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(total_sin_iva) as monto from nota_de_credito "
                + "where datediff(fecha_creacion,?) = 0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        assert(ok);
        Double ans = rs.getDouble("monto");

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getExpenses(String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto from gasto where datediff(?,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        Double ans = .0;
        if ( ok ){
            ans = rs.getDouble("monto");
        }
        
        c.close();
        rs.close();
        return ans;
    }

    protected static void markToUpdateFiscalNumbersToday() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update dia_operativo set actualizar_valores=1 "
                + "where reporteZ = 0 and datediff(curdate(),fecha)=0");
        stmt.executeUpdate();
        c.close();
    }

    protected static boolean isNeededtoUpdate() throws SQLException {
        boolean ans ;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select actualizar_valores from dia_operativo where datediff(curdate(),fecha)=0 and codigo_punto_de_venta= ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = (rs.getInt("actualizar_valores") == 1);
        }else{
            // I am JUST opening the pos.
            ans = false;
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void updateFiscalNumbers(Double cash, Double cn, Double debit, Double credit) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update dia_operativo set actualizar_valores = 0 , dinero_efectivo_impresora = ? , "
                + "dinero_tarjeta_credito_impresora = ? , dinero_tarjeta_debito_impresora = ? , nota_de_credito_impresora = ? where "
                + "datediff(curdate(),fecha)=0 and codigo_punto_de_venta= ? ");
        stmt.setDouble(1, cash);
        stmt.setDouble(2, credit);
        stmt.setDouble(3, debit);
        stmt.setDouble(4, cn);
        stmt.setString(5, Shared.getFileConfig("myId"));
        stmt.executeUpdate();
        c.close();
    }

    protected static void mirrorTableFastMode(String tableName){
        try {
            if (!Constants.isPos) {
                // Admin has no mirror
                return;
            }
            String cmd = "mysqldump -u " + Constants.dbUser + " -p"+
                    Constants.dbPassword + " -h " + Shared.getFileConfig("Server") + " "
                    + Constants.dbName + " " + tableName + " | mysql -u " + Constants.mirrordbUser
                    + " -p" + Constants.mirrordbPassword + " " + "-h " + Shared.getFileConfig("ServerMirror")
                    + " " + Constants.mirrorDbName ;
            FileWriter fstream = new FileWriter(Constants.tmpDir + Constants.scriptReplicateName);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(cmd);
            out.close();

            Process process = Runtime.getRuntime().exec(Constants.tmpDir + Constants.scriptReplicateName);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ( br.readLine() != null) {
                ;
            }

            File f = new File(Constants.tmpDir + Constants.scriptReplicateName);
            f.delete();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionDrivers.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    // WARNING!! NON-ESCAPED STRING
    protected static void mirrorTableSlowMode(String tableName) throws SQLException, PropertyVetoException{

        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Shared.getFileConfig("ServerMirror") + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection a = ConnectionDrivers.cpds.getConnection(); // This is updated
        Connection b = ConnectionDrivers.cpdsMirror.getConnection(); // This is outdated.

        PreparedStatement stmtA = a.prepareStatement("select * from "+ tableName + " limit 5000 "); // Getting the new data.
        ResultSet rsA = stmtA.executeQuery();
        ResultSetMetaData rsMetaDataA = rsA.getMetaData();

        PreparedStatement stmtB = b.prepareStatement("delete from " + tableName); // Good bye old data.

        stmtB.executeUpdate();

        while( rsA.next() ){
            String sql = "insert into " + tableName + " values ( ";
            for (int i = 0; i < rsMetaDataA.getColumnCount()-1; i++) {
                sql += "?,";
            }
            sql += "?)";

            PreparedStatement stmtNewB = b.prepareStatement(sql);

            for (int i = 0; i < rsMetaDataA.getColumnCount(); i++) {
                stmtNewB.setString(i+1, rsA.getString(i+1));
            }

            stmtNewB.executeUpdate();
        }

        rsA.close();
        a.close();
        b.close();

    }

    protected static void updateMoney() throws PropertyVetoException, SQLException{
        if ( !Constants.isPos ){
            // Admin does'nt have mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Shared.getFileConfig("ServerMirror") + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection b = ConnectionDrivers.cpds.getConnection();
        Connection a = ConnectionDrivers.cpdsMirror.getConnection();


        PreparedStatement stmtA = a.prepareStatement("select dinero_efectivo , dinero_tarjeta_debito , dinero_tarjeta_credito "
                + "from dia_operativo where datediff(fecha,curdate())=0 and codigo_punto_de_venta = ? ");
        stmtA.setString(1, Shared.getFileConfig("myId"));
        ResultSet rsA = stmtA.executeQuery();

        while( rsA.next() ){

            PreparedStatement stmtNewB = b.prepareStatement("update dia_operativo set dinero_efectivo = dinero_efectivo + ? , "
                    + "dinero_tarjeta_debito = dinero_tarjeta_debito + ? , dinero_tarjeta_credito = dinero_tarjeta_credito + ? where "
                    + "codigo_punto_de_venta = ? and datediff(fecha,curdate()) = 0");

            stmtNewB.setString(1, rsA.getString("dinero_efectivo"));
            stmtNewB.setString(2, rsA.getString("dinero_tarjeta_debito"));
            stmtNewB.setString(3, rsA.getString("dinero_tarjeta_credito"));
            stmtNewB.setString(4, Shared.getFileConfig("myId"));

            stmtNewB.executeUpdate();
        }

        stmtA = a.prepareStatement("delete from dia_operativo");
        stmtA.executeUpdate();

        rsA.close();
        a.close();
        b.close();
    }

    protected static void updateStock() throws PropertyVetoException, SQLException{
        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Shared.getFileConfig("ServerMirror") + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection b = ConnectionDrivers.cpds.getConnection();
        Connection a = ConnectionDrivers.cpdsMirror.getConnection();


        PreparedStatement stmtA = a.prepareStatement("select codigo_de_articulo,cantidad-devuelto as cantidad "
                + "from factura_contiene where cantidad-devuelto != 0  and sincronizado = 0");
        ResultSet rsA = stmtA.executeQuery();

        while( rsA.next() ){
            
            PreparedStatement stmtNewB = b.prepareStatement("update articulo set existencia_actual = existencia_actual - ? where codigo = ?");

            stmtNewB.setInt(1, rsA.getInt("cantidad"));
            stmtNewB.setString(2, rsA.getString("codigo_de_articulo"));

            stmtNewB.executeUpdate();
        }
        
        rsA.close();
        a.close();
        b.close();
    }

    protected static void cleanMirror(String tableName) throws PropertyVetoException, SQLException{
        if ( !Constants.isPos ){
            // Admin has no mirror
            return;
        }

        if ( !mirrorConnected ){ // just once =D
            cpdsMirror = new ComboPooledDataSource();
            cpdsMirror.setDriverClass("com.mysql.jdbc.Driver");
            String sT = "jdbc:mysql://" + Shared.getFileConfig("ServerMirror") + "/" + Constants.mirrorDbName;
            cpdsMirror.setJdbcUrl(sT);
            cpdsMirror.setUser(Constants.mirrordbUser);
            cpdsMirror.setPassword(Constants.mirrordbPassword);
            mirrorConnected = true;
        }

        Connection b = ConnectionDrivers.cpds.getConnection();
        Connection a = ConnectionDrivers.cpdsMirror.getConnection();


        PreparedStatement stmtA = a.prepareStatement("select * from "+ tableName + " where sincronizado = 0 ");
        ResultSet rsA = stmtA.executeQuery();
        ResultSetMetaData rsMetaDataA = rsA.getMetaData();

        while( rsA.next() ){
            String sql = "insert into " + tableName + " values (";
            for (int i = 0; i < rsMetaDataA.getColumnCount()-2; i++) {
                sql += "?,";
            }
            sql += "?)";

            PreparedStatement stmtNewB = b.prepareStatement(sql);

            for (int i = 0; i < rsMetaDataA.getColumnCount()-1; i++) {
                stmtNewB.setString(i+1, rsA.getString(i+1));
            }

            stmtNewB.executeUpdate();
            if ( tableName.equals("factura") ){
                ConnectionDrivers.renameReceipts();
            }else if ( tableName.equals("nota_de_credito") ){
                ConnectionDrivers.renameCN();
            }
        }


        PreparedStatement stmtA2 = a.prepareStatement("update " + tableName + " set sincronizado = 1");

        stmtA2.executeUpdate();

        rsA.close();
        a.close();
        b.close();
    }

    protected static void updateConfig(String k, String v) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update configuracion set `Value` = ? where `Key` = ?");
        stmt.setString(1, v);
        stmt.setString(2, k);
        stmt.executeUpdate();
        c.close();
    }

    protected static void setEnableSellWithoutStock(String i) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update configuracion set `Value` = ? where `Key` = 'sellWithoutStock'");
        stmt.setString(1, i);
        stmt.executeUpdate();
        c.close();
    }

    protected static boolean getEnableSellWithoutStock() throws SQLException {
        boolean ans = false;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select `Value` from configuracion where `Key` = 'sellWithoutStock'");
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = (rs.getString("Value").equals("1"));
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static List<String> getListMsg2Pos() throws SQLException {
        List<String> ans = new ArrayList<String>();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select `mensaje` from mensaje");
        ResultSet rs = stmt.executeQuery();
        while( rs.next() ){
            ans.add(rs.getString("mensaje"));
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void deleteAllMsgs() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from mensaje");
        stmt.executeUpdate();
        c.close();
    }

    protected static void createMsgs(TableModel model) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String msg = (String) model.getValueAt(i, 0) ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into mensaje ( mensaje ) values ( ? )");
            stmt.setString(1, msg);
            stmt.executeUpdate();
        }
        c.close();
    }

    protected static List<SimpleConfig> listConfig() throws SQLException{
        List<SimpleConfig> ans = new ArrayList<SimpleConfig>();
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select `Key` , `Value` from configuracion");
        ResultSet rs = stmt.executeQuery();
        while( rs.next() ){
            ans.add(new SimpleConfig(rs.getString("Key"),rs.getString("Value")));
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void deleteAllFrom(String table) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from " + table);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createConfig(TableModel model) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String key = (String) model.getValueAt(i, 0) ;
            String value = (String) model.getValueAt(i, 1) ;
            PreparedStatement stmt = c.prepareStatement(
                "update configuracion set `Value` = ? where (`Key` = ? or nombre = ? )");
            stmt.setString(1, value);
            stmt.setString(2, key);
            stmt.setString(3, key);
            stmt.executeUpdate();
        }
        c.close();
    }

    protected static void updateTotalFromPrinter(Double total, String z, String lReceipt, int quantReceiptsToday, String lastCN, int nNC, String day) throws SQLException {
        System.out.println("Dia Recibido = " + day);
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update dia_operativo set actualizar_valores = 0 , total_ventas = ? , "
                + "numero_reporte_z = ? , impresora = ? , codigo_ultima_factura = ? , ultima_actualizacion = now() , num_facturas = ? , "
                + "codigo_ultima_nota_credito = ? , numero_notas_credito = ? "
                + "where datediff(" + day + ",fecha) = 0 and codigo_punto_de_venta = ? ");
        stmt.setDouble(1, total);
        stmt.setString(2, z);
        stmt.setString(3, getMyPrinter());
        stmt.setString(4, lReceipt==null?"":lReceipt);
        stmt.setInt(5, quantReceiptsToday);
        stmt.setString(6, lastCN);
        stmt.setInt(7, nNC);
        stmt.setString(8, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        stmt = c.prepareStatement("update punto_de_venta set ultima_factura = ? , ultima_nota_de_credito = ? where identificador = ? ");
        stmt.setString(1, lReceipt);
        stmt.setString(2, lastCN);
        stmt.setString(3, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static JRDataSource getExpensesReport(String day) throws SQLException {

        String[] columnsArray = new String[3];
        for (int i = 0; i < 3 ; i++) {
            columnsArray[i] = i + "";
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select concepto , monto , descripcion from gasto where datediff( ? ,fecha)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[3];
            toAdd[0] = rs.getString("concepto");
            toAdd[1] = rs.getString("descripcion");
            toAdd[2] = new BigDecimal(rs.getDouble("monto"));
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    protected static JRDataSource getIncommingReport(String day) throws SQLException {
        String[] columnsArray = new String[4];
        for (int i = 0; i < 4; i++) {
            columnsArray[i] = i + "";
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select medio  as tipo, punto_de_venta_de_banco as descripcion ,"
                + " lote , monto_real as monto from pagos_punto_de_venta_banco where datediff(?,fecha)=0 " +
                "union " +
                "select 'Efectivo' as tipo , banco as descripcion , " +
                "numero as lote, monto from deposito where datediff(fecha,?)=0");
        stmt.setString(1, day);
        stmt.setString(2, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[4];
            toAdd[0] = rs.getString("tipo");
            toAdd[1] = rs.getString("descripcion");
            toAdd[2] = rs.getString("lote");
            toAdd[3] = new BigDecimal(rs.getDouble("monto"));
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    protected static JRDataSource getTotal(String day) throws SQLException {
        String[] columnsArray = new String[1];
        for (int i = 0; i < 1; i++) {
            columnsArray[i] = i + "";
        }
        DataSource dataSource = new DataSource(columnsArray);

        Object[] toAdd = new Object[1];
        toAdd[0] = ConnectionDrivers.getTotalDeclared(day) + "";
        dataSource.add(toAdd);
        
        return dataSource;
    }

    protected static Double getTotalPrinters(String day) throws SQLException{
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(total_ventas)* ? as total from dia_operativo where datediff(?,fecha)=0");
        stmt.setDouble(1, (Shared.getIva()+100.0)/100.0);
        stmt.setString(2, day);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble("total");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static JRDataSource getInitialFounds(String day) throws SQLException {
        String[] columnsArray = new String[3];
        for (int i = 0; i < 3; i++) {
            columnsArray[i] = i + "";
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select identificador_punto_de_venta, fecha , monto from movimiento_efectivo"
                + " where monto >= 0 and datediff(fecha,?) = 0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[3];
            toAdd[0] = rs.getString("identificador_punto_de_venta");
            toAdd[1] = rs.getString("fecha");
            toAdd[2] = new BigDecimal(rs.getDouble("monto"));
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    protected static JRDataSource getFiscalInfo(String day) throws SQLException {
        String[] columnsArray = new String[3];
        for (int i = 0; i < 3 ; i++) {
            columnsArray[i] = i + "";
        }
        DataSource dataSource = new DataSource(columnsArray);

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select impresora, numero_reporte_z , total_ventas " +
                "from dia_operativo where datediff(fecha,?)=0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Object[] toAdd = new Object[3];
            toAdd[0] = rs.getString("impresora");
            toAdd[1] = rs.getString("numero_reporte_z");
            toAdd[2] = new BigDecimal(Shared.round(rs.getDouble("total_ventas")*(Shared.getIva()+100.0)/100.0,2));
            dataSource.add(toAdd);
        }

        c.close();
        rs.close();

        return dataSource;
    }

    protected static String getThisPrinterId(String posId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        String ans = null;

        PreparedStatement stmt = c.prepareStatement("select impresora from "
                + "punto_de_venta where identificador = ? ");
        stmt.setString(1, posId);
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString(1);
        }

        c.close();

        return ans;
    }

    protected static String getOperativeDaysHtml(String day) throws SQLException{
        String ans = "";
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select total_ventas , impresora , numero_reporte_z ,"
                + " codigo_ultima_factura, num_facturas, codigo_ultima_nota_credito, numero_notas_credito, reporteZ, codigo_punto_de_venta "
                + "from dia_operativo where datediff(fecha,?) = 0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        int i = 0;
        while( rs.next() ){
            ans += "<tr>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("impresora");
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += Shared.round(rs.getDouble("total_ventas")*(Shared.getIva()+100.0)/100.0,2);
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("numero_reporte_z");
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("codigo_ultima_factura");
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("num_facturas");
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("codigo_ultima_nota_credito");
            ans += "</td>";
            if ( i%2 == 0 ){
                ans += "<td bordercolor=\"#000066\" style=\"background-color:#C1F2FF\">";
            }else{
                ans += "<td>";
            }
            ans += rs.getString("numero_notas_credito");
            ans += "</td>";
            ans += "</tr>";
            ++i;
        }

        c.close();

        return ans;
    }

    protected static List<ZFISDATAFISCAL> getOperativeDays(String day) throws SQLException {
        List<ZFISDATAFISCAL> ans = new ArrayList<ZFISDATAFISCAL>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select total_ventas , impresora , numero_reporte_z ,"
                + " codigo_ultima_factura, num_facturas, codigo_ultima_nota_credito, numero_notas_credito, reporteZ, codigo_punto_de_venta "
                + "from dia_operativo where datediff(fecha,?) = 0");
        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();
        System.out.println("MANDT\tIDTIENDA\tIDIMPFISCAL\tFECHA\tMONTO\tNUMREPZ\tULTFACTURA\tNUMFACD\tULTNOTACREDITO\tNUMNCD");
        while( rs.next() ){
            ZFISDATAFISCAL zfdf = new ZFISDATAFISCAL();
            zfdf.setMANDT(Constants.of.createZFISDATAFISCALMANDT(Constants.mant));
            System.out.print(Constants.mant + "\t");
            zfdf.setIDTIENDA(Constants.of.createZFISDATAFISCALIDTIENDA(Constants.storePrefix+Shared.getConfig("storeName")));
            System.out.print(Constants.storePrefix+Shared.getConfig("storeName") + "\t");
            if ( rs.getString("reporteZ").equals("0") ){
                System.out.println("No se ha sacado el reporte Z de la impresora de la caja " + rs.getString("codigo_punto_de_venta"));
                zfdf.setIDIMPFISCAL(Constants.of.createZFISDATAFISCALIDIMPFISCAL(ConnectionDrivers.getThisPrinterId( rs.getString("codigo_punto_de_venta"))));
                //System.out.print(rs.getString("impresora") + "\t");
                zfdf.setFECHA(Constants.of.createZFISDATAFISCALFECHA(day.replace("-", "")));
                System.out.print(day.replace("-", ""));
                zfdf.setMONTO(BigDecimal.ZERO);
                System.out.print(0 + "\t");
                zfdf.setNUMREPZ(Constants.of.createZFISDATAFISCALNUMREPZ("0"));
                System.out.print("0" + "\t");
                zfdf.setULTFACTURA(Constants.of.createZFISDATAFISCALULTFACTURA("0"));
                System.out.print("0" + "\t");
                zfdf.setNUMFACD(Constants.of.createZFISDATAFISCALNUMFACD("0"));
                System.out.print("0" + "\t");
                zfdf.setULTNOTACREDITO(Constants.of.createZFISDATAFISCALULTNOTACREDITO("0"));
                System.out.print("0" + "\t");
                zfdf.setNUMNCD(Constants.of.createZFISDATAFISCALNUMNCD("0"));
                System.out.print("0");
                ans.add(zfdf);
            }else{
                zfdf.setIDIMPFISCAL(Constants.of.createZFISDATAFISCALIDIMPFISCAL(rs.getString("impresora")));
                System.out.print(rs.getString("impresora") + "\t");
                zfdf.setFECHA(Constants.of.createZFISDATAFISCALFECHA(day.replace("-", "")));
                System.out.print(day.replace("-", ""));
                zfdf.setMONTO(new BigDecimal(Shared.round(rs.getDouble("total_ventas")*(Shared.getIva()+100.0)/100.0,2)));
                System.out.print(Shared.round(rs.getDouble("total_ventas")*(Shared.getIva()+100.0)/100.0,2) + "\t");
                zfdf.setNUMREPZ(Constants.of.createZFISDATAFISCALNUMREPZ(rs.getString("numero_reporte_z")));
                System.out.print(rs.getString("numero_reporte_z") + "\t");
                zfdf.setULTFACTURA(Constants.of.createZFISDATAFISCALULTFACTURA(rs.getString("codigo_ultima_factura")));
                System.out.print(rs.getString("codigo_ultima_factura") + "\t");
                zfdf.setNUMFACD(Constants.of.createZFISDATAFISCALNUMFACD(rs.getString("num_facturas")));
                System.out.print(rs.getString("num_facturas") + "\t");
                zfdf.setULTNOTACREDITO(Constants.of.createZFISDATAFISCALULTNOTACREDITO(rs.getString("codigo_ultima_nota_credito")));
                System.out.print(rs.getString("codigo_ultima_nota_credito") + "\t");
                zfdf.setNUMNCD(Constants.of.createZFISDATAFISCALNUMNCD(rs.getString("numero_notas_credito")));
                System.out.print(rs.getString("numero_notas_credito"));
                ans.add(zfdf);
            }
            System.out.println("");
        }
        c.close();

        return ans;
    }

    protected static IXMLElement createFiscalData(String day) throws SQLException{

        XMLElement xml = new XMLElement("FiscalData");
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select total_ventas , impresora , numero_reporte_z ,"
                + " codigo_ultima_factura, num_facturas, codigo_ultima_nota_credito, numero_notas_credito, reporteZ, codigo_punto_de_venta "
                + "from dia_operativo where datediff(fecha,?) = 0");

        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while( rs.next() ){
            IXMLElement child = xml.createElement("I");
            xml.addChild(child);
            if ( rs.getString("reporteZ").equals("0") ){
                 System.out.println("No se ha sacado el reporte Z de la impresora de la caja " + rs.getString("codigo_punto_de_venta"));
                 child.setAttribute("printer", ConnectionDrivers.getThisPrinterId( rs.getString("codigo_punto_de_venta")));
                 child.setAttribute("monto", "0");
                 child.setAttribute("reporteZ", "0");
                 child.setAttribute("lastR", "0");
                 child.setAttribute("numR", "0");
                 child.setAttribute("lastCN", "0");
                 child.setAttribute("numCN", "0");
            }else{
                child.setAttribute("printer", rs.getString("impresora") );
                child.setAttribute("monto", Shared.round(rs.getDouble("total_ventas")*(Shared.getIva()+100.0)/100.0,2) + "");
                child.setAttribute("reporteZ", rs.getString("numero_reporte_z"));
                child.setAttribute("lastR", rs.getString("codigo_ultima_factura"));
                child.setAttribute("numR", rs.getString("num_facturas"));
                child.setAttribute("lastCN", rs.getString("codigo_ultima_nota_credito"));
                child.setAttribute("numCN", rs.getString("numero_notas_credito"));
                System.out.println(" Reporte Z === "  + rs.getString("numero_reporte_z"));
            }
        }

        c.close();

        return xml;
    }

    protected static List<Receipt> listOkReceipts(String day) throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_interno, estado, fecha_creacion, "
                + "fecha_impresion, codigo_de_cliente , total_sin_iva, total_con_iva, "
                + "descuento_global, iva, impresora, numero_fiscal, "
                + "numero_reporte_z, codigo_de_usuario, cantidad_de_articulos , identificador_turno , codigo_interno_alternativo "
                + "from factura where estado='Facturada' and datediff(fecha_creacion,?) = 0 order by impresora , cast(numero_fiscal as signed)");

        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Receipt r = new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            rs.getDouble("descuento_global"),
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2Receipt(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno"),
                            rs.getString("codigo_interno_alternativo")
                        );
            if ( !r.getItems().isEmpty() ){
                ans.add(r);
            }
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static List<Receipt> listOkCN(String day) throws SQLException{
        List<Receipt> ans = new ArrayList<Receipt>();

        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select nc.codigo_interno, nc.estado, nc.fecha_creacion, "
                + "nc.fecha_impresion, fac.codigo_de_cliente , nc.total_sin_iva, nc.total_con_iva, "
                + "nc.iva, nc.impresora, nc.numero_fiscal, "
                + "nc.numero_reporte_z, nc.codigo_de_usuario, nc.cantidad_de_articulos , nc.identificador_turno , nc.codigo_interno_alternativo "
                + "from nota_de_credito nc , factura fac where nc.codigo_factura = fac.codigo_interno and nc.estado='Nota' "
                + "and datediff(nc.fecha_creacion,?) = 0 order by nc.impresora , cast(nc.numero_fiscal as signed)");

        stmt.setString(1, day);
        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            Receipt r = new Receipt(
                            rs.getString("codigo_interno"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_creacion"),
                            rs.getTimestamp("fecha_impresion"),
                            rs.getString("codigo_de_cliente"),
                            rs.getDouble("total_sin_iva"),
                            rs.getDouble("total_con_iva"),
                            .0,
                            rs.getDouble("iva"),
                            rs.getString("impresora"),
                            rs.getString("numero_fiscal"),
                            rs.getString("numero_reporte_z"),
                            rs.getString("codigo_de_usuario"),
                            rs.getInt("cantidad_de_articulos"),
                            listItems2CN(rs.getString("codigo_interno")),
                            rs.getString("identificador_turno"),
                            rs.getString("codigo_interno_alternativo")
                        );
            if ( !r.getItems().isEmpty() ){
                ans.add(r);
            }
        }
        c.close();
        rs.close();

        return ans;
    }

    protected static void updateDiscount(String itemId, String discount) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update articulo set descuento = ? where codigo = ?");
        stmt.setString(1, discount);
        stmt.setString(2, itemId);
        stmt.executeUpdate();
        c.close();
    }

    protected static void setZDone(String day) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo "
                + "set reporteZ = 1 "
                + "where codigo_punto_de_venta = ? and datediff( "+ day +" , fecha ) = 0 ");

        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    //WARNING. NON ESCAPED STRINGS
    protected static Double getSumTotalWithIva(String myDay, String table, String status, boolean withDiscount, String pos) throws SQLException {
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(total_sin_iva " + (withDiscount?"-total_sin_iva*descuento_global":"") + ")"
                + " as total from " + table + " where datediff(\'" + myDay + "\',fecha_creacion) = 0 and estado = ? " + (pos==null?"":"and identificador_pos = ? ") );
        stmt.setString(1, status);
        if ( pos != null ){
            stmt.setString(2,pos);
        }
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        System.out.println("Ok = " + ok );
        if ( ok ){
            ans = rs.getDouble("total");
            System.out.println("Ans = " + ans);
        }

        c.close();
        rs.close();
        System.out.println("Respuesta : = " + ans);
        return ans;
    }

    protected static Double getTotalDeclared(String myDay) throws SQLException {
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(total_ventas) as total from dia_operativo where fecha=?");
        stmt.setString(1, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble("total");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void updateLastReceipt(String lastReceipt) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set ultima_factura = ? where identificador = ? ");
        stmt.setString(1, lastReceipt);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();

        c.close();
    }

    protected static void updateLastCN(Connection c, String lastCN) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set ultima_nota_de_credito = ? where identificador = ? ");
        stmt.setString(1, lastCN);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();
    }

    protected static void updateLastCN(String lastCN) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update punto_de_venta set ultima_nota_de_credito = ? where identificador = ? ");
        stmt.setString(1, lastCN);
        stmt.setString(2, Shared.getFileConfig("myId"));
        stmt.executeUpdate();
        c.close();
    }

    protected static String getLastReceipt() throws SQLException {
        String ans = "";
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select ultima_factura from punto_de_venta where identificador = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString("ultima_factura");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static String getLastCN() throws SQLException {
        String ans = "";
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select ultima_nota_de_credito from punto_de_venta where identificador = ? ");
        stmt.setString(1, Shared.getFileConfig("myId"));
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString("ultima_nota_de_credito");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void updateItemsDetails(Item item, Connection c) throws SQLException{
        PreparedStatement stmt = c.prepareStatement("insert IGNORE into codigo_de_barras( codigo_de_articulo , codigo_de_barras ) values ( ? , ? )");
        stmt.setString(1, item.getCode());
        stmt.setString(2, item.getMainBarcode());
        stmt.executeUpdate();
        stmt = c.prepareStatement("insert IGNORE into precio( codigo_de_articulo , monto , fecha  ) values ( ? , ? , curdate() )");
        stmt.setString(1, item.getCode());
        stmt.setDouble(2, item.getPrice().get(0).getQuant());
        stmt.executeUpdate();
    }

    protected static void updateItems(List<Item> items) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        Connection c2 = ConnectionDrivers.cpds.getConnection();
        for (Item item : items) {
            PreparedStatement stmt = c.prepareStatement("update articulo set "
                    + "codigo_de_barras = ? "
                    + "where codigo = ? ");
            stmt.setString(1, item.getMainBarcode());
            stmt.setString(2, item.getCode());
            int ans = stmt.executeUpdate();
            if ( ans > 0 ){
                updateItemsDetails(item, c2);
            }
        }

        c.close();
        c2.close();
    }

    protected static void updateMovements(List<Movement> movements, TreeMap<String, Item> newItemMapping) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (Movement movement : movements) {
            PreparedStatement stmt = c.prepareStatement("select * from movimiento_inventario where identificador>=? and length(identificador)=5");
            stmt.setString(1, movement.getId());
            ResultSet rs = stmt.executeQuery();
            if ( rs.next() ){
                MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Traslado con código " + movement.getCode() + " ya fue cargado o traslados posteriores se cargaron anteriormente, será ignorado.");
                msb.show(Shared.getMyMainWindows());
                continue;
            }
            stmt = c.prepareStatement("insert into movimiento_inventario "
                    + "( identificador , fecha , descripcion, codigo, almacen ) values( ? , ? , ? , ? , ? )");
            stmt.setString(1, movement.getId());
            stmt.setDate(2, movement.getDate());
            stmt.setString(3, movement.getDescription());
            stmt.setString(4, movement.getCode());
            stmt.setString(5, movement.getStoreId());
            stmt.executeUpdate();
            for (ItemQuant itemMovement : movement.getItems()) {
                if ( newItemMapping.get(itemMovement.getItemId()) != null ){
                    stmt = c.prepareStatement("insert into detalles_movimientos "
                        + "( identificador_movimiento , codigo_articulo , cantidad_articulo ) values( ? , ? , ? )");
                    stmt.setString(1, movement.getId());
                    stmt.setString(2, itemMovement.getItemId());
                    stmt.setInt(3, itemMovement.getQuant());
                    stmt.executeUpdate();
                    stmt = c.prepareStatement("update articulo set existencia_actual = existencia_actual + ? where codigo = ? ");
                    stmt.setString(2, itemMovement.getItemId());
                    stmt.setInt(1, itemMovement.getQuant());
                    int ans = stmt.executeUpdate();
                    if ( ans == 0 ){
                        insertItem(newItemMapping.get(itemMovement.getItemId()));
                        stmt = c.prepareStatement("update articulo set existencia_actual = existencia_actual + ? where codigo = ? ");
                        stmt.setString(2, itemMovement.getItemId());
                        stmt.setInt(1, itemMovement.getQuant());
                        stmt.executeUpdate();
                    }
                }else{
                    MessageBox msb = new MessageBox(MessageBox.SGN_IMPORTANT, "Objeto en el traslado que no contiene descripción: " + itemMovement.getItemId() );
                    msb.show(Shared.getMyMainWindows());
                }
            }
            
        }

        c.close();
    }

    private static void insertItem(Item item) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into articulo ( codigo , descripcion , fecha_registro , "
                + "marca , sector, codigo_sublinea , codigo_de_barras , modelo , unidad_venta, unidad_compra, "
                + "existencia_actual, bloqueado, imagen, descuento ) values (?,?,now(),?,?,?,?,?,?,?,0,?,?,?)");
        stmt.setString(1, item.getCode());
        stmt.setString(2, item.getDescription());
        stmt.setString(3 , item.getMark());
        stmt.setString(4, item.getSector());
        stmt.setString(5, item.getSublineCode());
        stmt.setString(6, item.getMainBarcode());
        stmt.setString(7, item.getModel());
        stmt.setString(8, item.getSellUnits());
        stmt.setString(9, item.getBuyUnits());
        stmt.setBoolean(10, item.isStatus());
        stmt.setString(11, Shared.getConfig("photoDir") + item.getCode() + ".JPG");
        stmt.setString(12, "0");
        stmt.executeUpdate();
        updateItemsDetails(item, c );
        c.close();

    }

    protected static void modifyMoney(Double diffMoney) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("insert into movimiento_efectivo ( identificador_punto_de_venta , fecha , monto )"
                + " values (?,now(),?)");
        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.setDouble(2, diffMoney);
        stmt.executeUpdate();
        c.close();
    }

    protected static boolean allZready(String myDay) throws SQLException {
        boolean ans = false;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select count(*) as c from dia_operativo where datediff( ? , fecha ) = 0 and reporteZ = 0");
        stmt.setString(1, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString("c").equals("0");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static Integer getQuantCN(String myDay) throws SQLException {
        Integer ans = 0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(numero_notas_credito) as ans from dia_operativo where datediff( ? , fecha ) = 0");
        stmt.setString(1, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getInt("ans");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalIncomming(String myDay) throws SQLException {
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto"
                + " from (select sum(monto_real) as monto from pagos_punto_de_venta_banco where datediff(?,fecha)=0"
                + " union select sum(monto) "
                + "as monto from deposito where datediff(fecha,?)=0) as myTable");
        stmt.setString(1, myDay);
        stmt.setString(2, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble("monto");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static Double getTotalAMinusC(String myDay) throws SQLException {
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select sum(monto) as monto from "
                + "(select -1*sum(monto) as monto from gasto where datediff(fecha,?)=0 "
                + "union select sum(monto) as monto from movimiento_efectivo where datediff(?,fecha)=0 "
                + "and monto > 0) as myTable");
        stmt.setString(1, myDay);
        stmt.setString(2, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble("monto");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void deleteAllPayments(String myDay) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from pagos_punto_de_venta_banco where datediff(?,fecha)=0");
        stmt.setString(1, myDay);
        stmt.executeUpdate();
        c.close();
    }

    protected static void createPayments(DefaultTableModel model, String myDay) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            PreparedStatement stmt = c.prepareStatement(
                "insert into pagos_punto_de_venta_banco ( fecha, punto_de_venta_de_banco "
                + ", lote , medio , declarado , monto_real ) values( ?,?,?,?,?,?)");
            stmt.setString(1, myDay);
            stmt.setString(2, (String) model.getValueAt(i, 0));
            stmt.setString(3, (String) model.getValueAt(i, 1));
            stmt.setString(4, (String) model.getValueAt(i, 2));
            stmt.setString(5, (String) model.getValueAt(i, 3));
            stmt.setDouble(6, (Double) model.getValueAt(i, 4));
            stmt.executeUpdate();
        }

        c.close();
    }

    protected static boolean wasClosed(String myDay) throws SQLException {
        boolean ans = false;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select cerrado from dia_operativo where datediff(fecha,?)=0");
        stmt.setString(1, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getBoolean("cerrado");
        }

        c.close();
        rs.close();
        return ans;
    }

    protected static void closeThisDay(String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update dia_operativo set cerrado = 1 where datediff(fecha,?)=0");
        stmt.setString(1, myDay);
        stmt.executeUpdate();

        c.close();
    }

    protected static boolean previousClosed(String myDay) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select * from dia_operativo where datediff( ? , fecha ) > 0 and cerrado = 0");
        stmt.setString(1, myDay);
        ResultSet rs = stmt.executeQuery();

        boolean ok = !rs.next();
        
        c.close();
        rs.close();
        return ok;
    }

    protected static String configName(String key) throws SQLException{
        String ans = null;
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select nombre from configuracion where `Key` = ? ");
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString("nombre");
        }

        c.close();
        return ans;
    }

    // WARNING NON_ESCAPE QUERY
    static Integer getQuant( String pos, String field, String day) throws SQLException {
        Integer ans = 0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select " + field + " as ans from dia_operativo where datediff( "+ day + " , fecha ) = 0 "
                + "and codigo_punto_de_venta = ? ");
        stmt.setString(1, pos);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getInt("ans");
        }

        c.close();
        rs.close();
        return ans;
    }

    static Double getTotalDeclaredPos( String pos, String day) throws SQLException{
        Double ans = .0;
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select total_ventas as ans from dia_operativo "
                + "where codigo_punto_de_venta = ? and datediff(fecha,"+day+")=0");
        stmt.setString(1, pos);
        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble("ans");
        }

        ans *= Shared.getIva()/100.0+1.0;
        c.close();
        rs.close();
        return ans;
    }

    protected static String getLastMM() throws SQLException{
        String ans = null;
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select max(identificador) from movimiento_inventario");
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString(1);
        }

        c.close();
        String[] ansA = ans.split("-");

        return ansA[ansA.length-1];
    }

    protected static String createNewMovement(Connection c , String xmlMovement) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException, IOException{

        Shared.itemsNeeded = new LinkedList<XMLElement>();

        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        IXMLReader reader = StdXMLReader.stringReader(xmlMovement);
        parser.setReader(reader);
        IXMLElement xml = (IXMLElement) parser.parse();

        System.out.println(xml.getName());
        TreeSet<String> movements = new TreeSet<String>();

        PreparedStatement stmtDetailsMovements = c.prepareStatement("insert into detalles_movimientos"
                    + "(identificador_movimiento,codigo_articulo,cantidad_articulo,tipo) values ( ? , ? , ? , ? )");
        PreparedStatement stmtCurrentStock = c.prepareStatement("update articulo set existencia_actual = existencia_actual + ? where codigo = ? ");
        for (Object x : xml.getChildren()) {
            XMLElement xmlI = (XMLElement)x;

            // TODO QUITAR
            /*if ( !xmlI.getAttribute("MBLNR").equals("4900458135") && !xmlI.getAttribute("MBLNR").equals("4900458134")
                    && !xmlI.getAttribute("MBLNR").equals("4900458133") && !xmlI.getAttribute("MBLNR").equals("4900458130") && !xmlI.getAttribute("MBLNR").equals("4900458129")){
                continue;
            }*/
            /*if (
                    !xmlI.getAttribute("MBLNR").equals("4900447579")&& !xmlI.getAttribute("MBLNR").equals("4900447580")
                    && !xmlI.getAttribute("MBLNR").equals("4900447581")){
                continue;
            }*/
            int reason = Shared.calculateReason(xmlI.getAttribute("BWART"), xmlI.getAttribute("SHKZG"));
            // TODO QUITAR
            //reason *= -1;

            System.out.println("MBLNR = " + xmlI.getAttribute("MBLNR") + " reason = " + reason + " codigo_articulo = " + xmlI.getAttribute("MATNR"));
            
            stmtDetailsMovements.setString(1, xmlI.getAttribute("MBLNR"));
            stmtDetailsMovements.setString(2, xmlI.getAttribute("MATNR"));
            stmtDetailsMovements.setInt(3, reason * Integer.parseInt(xmlI.getAttribute("MENGE").split("\\.")[0]));
            stmtDetailsMovements.setString(4, xmlI.getAttribute("BWART"));
            stmtDetailsMovements.executeUpdate();

            movements.add(xmlI.getAttribute("MBLNR"));

            System.out.println("Movimiento " + reason + " articulo " + xmlI.getAttribute("MATNR"));
            if ( reason == 0 ){
                // we are in problems... =(
            }else{
                stmtCurrentStock.setString(2, xmlI.getAttribute("MATNR"));
                stmtCurrentStock.setInt(1, reason * Integer.parseInt(xmlI.getAttribute("MENGE").split("\\.")[0]));
                int ans = stmtCurrentStock.executeUpdate();
                if ( ans == 0 ){
                    Shared.itemsNeeded.add(xmlI);
                }
            }
        }

        xml = null;

        PreparedStatement stmtInsert = c.prepareStatement("insert into movimiento_inventario (identificador , fecha , descripcion , codigo , almacen ) "
                    + "values (? , now() , ? , ? , ?)");
        Iterator<String> itrs = movements.iterator();
        while(itrs.hasNext()){
            String id = itrs.next();
            
            stmtInsert.setString(1, id);
            stmtInsert.setString(2, "Nuevo Movimiento Inventario");
            stmtInsert.setString(3, id);
            stmtInsert.setString(4, "");
            stmtInsert.executeUpdate();
        }

        movements = null;
        

        XMLElement ans = new XMLElement("ITEMSNEEDED");

        for( XMLElement it : Shared.itemsNeeded){
            IXMLElement ansi = ans.createElement("ITEM");
            ans.addChild(ansi);
            ansi.setContent( it.getAttribute("MATNR") );
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLWriter xmlw = new XMLWriter(baos);
        xmlw.write(ans);
        String tAns = baos.toString() + "";
        baos = null;
        xmlw = null;
        return tAns;
    }

    static void createItems(Connection c, String ansDescriptions, boolean checkReason) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException {

        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        IXMLReader reader = StdXMLReader.stringReader(ansDescriptions);
        parser.setReader(reader);
        System.out.println("Recibido " + ansDescriptions);
        IXMLElement xml = (IXMLElement) parser.parse();

        parser = null;
        reader = null;

        TreeSet<String> itemsNeededJustOnce = new TreeSet<String>();

        System.out.println("Justo antes de agregar los articulos...");
        System.out.println("Descripciones ..." + ansDescriptions);
        PreparedStatement stmtItem = c.prepareStatement("insert into articulo ( codigo , descripcion , fecha_registro , "
                    + "codigo_de_barras , modelo , unidad_venta, "
                    + "existencia_actual, bloqueado, imagen, descuento ) values (?,?,now(),?,?,?,0,0,?,?)");
        PreparedStatement stmtBarcode = c.prepareStatement("insert IGNORE into codigo_de_barras(codigo_de_articulo,codigo_de_barras) values(?,?)");
        XMLElement xmlI = null;
        for (Object x : xml.getChildren()) {
            xmlI = (XMLElement)x;
            if ( !itemsNeededJustOnce.contains(xmlI.getAttribute("MATNR","")) ){
                stmtItem.setString(1, xmlI.getAttribute("MATNR",""));
                stmtItem.setString(2, xmlI.getAttribute("MAKTG",""));
                stmtItem.setString(3, xmlI.getAttribute("EAN11",""));
                stmtItem.setString(4, xmlI.getAttribute("MATKL",""));
                stmtItem.setString(5, xmlI.getAttribute("MSEH3",""));
                stmtItem.setString(6, Shared.getConfig("photoDir") + xmlI.getAttribute("MATNR","") + ".JPG");
                stmtItem.setString(7, "0");
                stmtItem.executeUpdate();

                stmtBarcode.setString(1, xmlI.getAttribute("MATNR",""));
                stmtBarcode.setString(2, xmlI.getAttribute("EAN11",""));
                stmtBarcode.executeUpdate();

                itemsNeededJustOnce.add(xmlI.getAttribute("MATNR",""));

            }
            xmlI = null;
        }

        itemsNeededJustOnce = null;
        xml = null;
        parser = null;
        reader = null;
        ansDescriptions = null;

        Iterator<XMLElement> itr = Shared.itemsNeeded.iterator();
        stmtItem = c.prepareStatement("update articulo set existencia_actual = existencia_actual + ? where codigo = ? ");
        while ( itr.hasNext() ){
            xmlI = itr.next();
            int reason = 1;
            if ( checkReason ){
                reason = Shared.calculateReason(xmlI.getAttribute("BWART",""), xmlI.getAttribute("SHKZG",""));
            }
            if ( reason == 0 ){
                // we are in problems... =(
            }else{
                stmtItem.setString(2, xmlI.getAttribute("MATNR",""));
                stmtItem.setInt(1, reason * Integer.parseInt(xmlI.getAttribute("MENGE","").split("\\.")[0]));
                int ans = stmtItem.executeUpdate();
                if ( ans == 0 ){
                    // We are in problemas again... ='(
                }
            }
        }
    }

    static void setPrices(Connection c, String ansPricesDiscounts) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException {
        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        IXMLReader reader = StdXMLReader.stringReader(ansPricesDiscounts);
        parser.setReader(reader);
        IXMLElement xml = (IXMLElement) parser.parse();

        PreparedStatement stmtUpdate = c.prepareStatement("update articulo set descuento = ? where codigo = ? ");
        PreparedStatement stmtDelete = c.prepareStatement("delete from precio where codigo_de_articulo = ? and fecha = curdate() ");
        PreparedStatement stmtInsert = c.prepareStatement("insert into precio ( codigo_de_articulo , monto , fecha ) values ( ? , ? , curdate() ) ");
        XMLElement xmlI = null;
        Double dis = null;
        String disS = null;
        parser = null;
        reader = null;
        System.out.println("Comenzo a fijar los descuentos...");
        for (Object x : xml.getChildren()) {

            xmlI = (XMLElement)x;

            if ( xmlI.getAttribute("KONWA","").equals("%") ) {
                dis = -1*Double.parseDouble(xmlI.getAttribute("KBETR",""))/10;
                disS = (dis+"").substring(0,Math.min((dis+"").length(), 5));

                stmtUpdate.setString(1, disS);
                stmtUpdate.setString(2, xmlI.getAttribute("VAKEY","").substring(4));
                stmtUpdate.executeUpdate();
            }else{
                stmtDelete.setString(1, xmlI.getAttribute("VAKEY","").substring(6));
                stmtDelete.executeUpdate();
                stmtInsert.setString(1, xmlI.getAttribute("VAKEY","").substring(6));
                stmtInsert.setString(2, Double.parseDouble(xmlI.getAttribute("KBETR",""))+"");
                stmtInsert.executeUpdate();
            }

            xmlI = null;
        }
        System.out.println("Finalizo los descuentos!");

    }

    static void setLastUpdateNow() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update configuracion set `Value` = replace(curdate(),'-','') where `Key` = 'lastPriceUpdate'");
        stmt.executeUpdate();
        initializeConfig();
        c.close();
    }

    static void ensureTotalReceipt(Connection c, String actualId) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("select sum((precio_venta - precio_venta*descuento/100)*cantidad) as total from factura_contiene where codigo_interno_factura = ? ");
        stmt.setString(1, actualId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        Double total_sin_iva = rs.getDouble("total");
        Double iva = new Price(null,total_sin_iva).getIva().getQuant();
        Double total_con_iva = new Price(null , total_sin_iva).plusIva().getQuant();
        stmt = c.prepareStatement("update factura set total_sin_iva = ? , total_con_iva = ? , iva = ? where codigo_interno = ?");
        stmt.setDouble(1, total_sin_iva);
        stmt.setDouble(2, total_con_iva);
        stmt.setDouble(3, iva);
        stmt.setString(4, actualId);

        stmt.executeUpdate();
    }

    static void setAllFiscalData(String actualId, String serial, String z, String lastFiscalNumber, Client client) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        ConnectionDrivers.setFiscalData(c, actualId, serial , z , lastFiscalNumber);
        if ( client != null ){
            ConnectionDrivers.setClient(c,client,actualId);
        }
        ConnectionDrivers.setPritingHour(c,actualId, "factura");
        ConnectionDrivers.finishReceipt(c, actualId);
        ConnectionDrivers.ensureTotalReceipt(c, actualId);

        c.close();
    }

    static void setAllFiscalDataCN(String actualId, String serial, String z, String lastFiscalNumber) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        ConnectionDrivers.setFiscalDataCN(c, actualId, serial, z , lastFiscalNumber);
        ConnectionDrivers.setPritingHour(c, actualId, "nota_de_credito");
        ConnectionDrivers.updateLastCN(c,lastFiscalNumber);
        c.close();
    }

    static void fixWrongReceipts() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("update factura set estado=\'Facturada\' where numero_fiscal is not null and estado=\'Pedido\'");
        stmt.executeUpdate();

        c.close();
    }

    static String getDate4NCR() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        String ans = null;

        PreparedStatement stmt = c.prepareStatement("select date_format(now(),'%d %m %Y %h %i %s %p')");
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString(1);
        }

        c.close();

        return ans;
    }

    static String getInitialStock(Connection c, String xmlMovement) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException, IOException{
        Shared.itemsNeeded = new LinkedList<XMLElement>();

        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        IXMLReader reader = StdXMLReader.stringReader(xmlMovement);
        parser.setReader(reader);
        IXMLElement xml = (IXMLElement) parser.parse();

        System.out.println(xml.getName());
        TreeSet<String> movements = new TreeSet<String>();

        for (Object x : xml.getChildren()) {
            XMLElement xmlI = (XMLElement)x;

            // TODO QUITAR
            /*if ( !xmlI.getAttribute("MBLNR").equals("4900392898") && !xmlI.getAttribute("MBLNR").equals("4900392871") ){
                continue;
            }*/
            int reason = 1;

            System.out.println("MBLNR = " + xmlI.getAttribute("MBLNR") + " reason = " + reason + " codigo_articulo = " + xmlI.getAttribute("MATNR") + " MENGE = " + xmlI.getAttribute("MENGE"));
            PreparedStatement stmt = c.prepareStatement("insert into detalles_movimientos"
                    + "(identificador_movimiento,codigo_articulo,cantidad_articulo,tipo) values ( ? , ? , ? , ? )");
            stmt.setString(1, xmlI.getAttribute("MBLNR"));
            stmt.setString(2, xmlI.getAttribute("MATNR"));
            stmt.setInt(3, reason * Integer.parseInt(xmlI.getAttribute("MENGE").split("\\.")[0]));
            stmt.setString(4, xmlI.getAttribute("BWART"));
            stmt.executeUpdate();

            movements.add(xmlI.getAttribute("MBLNR"));

            if ( reason == 0 ){
                // we are in problems... =(
            }else{
                stmt = c.prepareStatement("update articulo set existencia_actual = existencia_actual + ? where codigo = ? ");
                stmt.setString(2, xmlI.getAttribute("MATNR"));
                stmt.setInt(1, reason * Integer.parseInt(xmlI.getAttribute("MENGE").split("\\.")[0]));
                int ans = stmt.executeUpdate();
                if ( ans == 0 ){
                    Shared.itemsNeeded.add(xmlI);
                }
            }
        }

        Iterator<String> itrs = movements.iterator();
        while(itrs.hasNext()){
            String id = itrs.next();
            PreparedStatement stmt = c.prepareStatement("insert into movimiento_inventario (identificador , fecha , descripcion , codigo , almacen ) "
                    + "values (? , now() , ? , ? , ?)");
            stmt.setString(1, id);
            stmt.setString(2, "Stock Inicial");
            stmt.setString(3, id);
            stmt.setString(4, "");
            stmt.executeUpdate();
        }


        XMLElement ans = new XMLElement("ITEMSNEEDED");

        for( XMLElement it : Shared.itemsNeeded){
            IXMLElement ansi = ans.createElement("ITEM");
            ans.addChild(ansi);
            ansi.setContent( it.getAttribute("MATNR") );
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLWriter xmlw = new XMLWriter(baos);
        xmlw.write(ans);
        String tAns = baos.toString() + "";
        baos = null;
        xmlw = null;
        return tAns;
    }

    static String checkAllZReport(String printerId , String zReportId) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        String ans = null;

        PreparedStatement stmt = c.prepareStatement("select distinct fecha, "
                + "factura.numero_reporte_z from dia_operativo, factura where reporteZ=0 "
                + "and codigo_punto_de_venta = ? and datediff(fecha,fecha_creacion)=0 and "
                + "identificador_pos=codigo_punto_de_venta and estado='Facturada' and"
                + " factura.numero_reporte_z=? and factura.impresora=? and fecha != curdate() order "
                + "by fecha desc");
        stmt.setString(1, Shared.getFileConfig("myId"));
        stmt.setString(2, zReportId);
        stmt.setString(3, printerId);
        ResultSet rs = stmt.executeQuery();
        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getString(1);
        }

        c.close();

        return ans;
    }

    static double getTotalPrinter(String day , String pos) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        double ans = .0;

        PreparedStatement stmt = c.prepareStatement("select total_ventas from dia_operativo where fecha = " + day + " and codigo_punto_de_venta = ? ");

        stmt.setString(1, pos);

        ResultSet rs = stmt.executeQuery();

        boolean ok = rs.next();
        if ( ok ){
            ans = rs.getDouble(1);
        }

        c.close();
        return ans;
    }

    static void updateEmployees(String xmlEmployees) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        c.setAutoCommit(false);

        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        IXMLReader reader = StdXMLReader.stringReader(xmlEmployees);
        parser.setReader(reader);
        IXMLElement xml = (IXMLElement) parser.parse();

        PreparedStatement stmt = c.prepareStatement("delete from empleado");
        stmt.executeUpdate();
        for (Object x : xml.getChildren()) {
            XMLElement xmlI = (XMLElement)x;

            stmt = c.prepareStatement("insert into empleado (codigo,nombre_completo, departamento) values(?,?,?)");
            stmt.setString(1, xmlI.getAttribute("c"));
            stmt.setString(2, xmlI.getAttribute("n"));
            stmt.setString(3, xmlI.getAttribute("d"));
            stmt.executeUpdate();
        }

        try{
            c.commit();
        }catch(SQLException ex){
            System.out.println("Rolling back... ha ocurrido un error.");
            try{
                c.rollback();
            }catch(Exception ex2){
                // So bad, we are in problems =(
            }

        }finally{
            if ( c != null && !c.isClosed()){
                c.close();
            }
        }
    }

    static boolean existsEmployCode(String employId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select * from empleado where codigo = ? ");
        pstmt.setString(1, employId);
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return false;
        }

        c.close();
        rs.close();
        return true;
    }

    static Employ getEmploy(String employId) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select codigo, nombre_completo from empleado where codigo = ? ");
        pstmt.setString(1, employId);
        ResultSet rs = pstmt.executeQuery();

        if ( ! rs.next() ){
            rs.close();
            c.close();
            return null;
        }

        Employ ans = new Employ(rs.getString("codigo"), rs.getString("nombre_completo"));
        c.close();
        rs.close();
        return ans;
    }

    static List<Employ> getAllEmployees() throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement pstmt = c.prepareStatement("select codigo, nombre_completo from empleado");
        ResultSet rs = pstmt.executeQuery();

        List<Employ> ans = new LinkedList<Employ>();

        while( rs.next() ){
            ans.add(new Employ(rs.getString("codigo"), rs.getString("nombre_completo")));
        }

        c.close();
        rs.close();
        return ans;
    }

    static void saveTemplate(String code, byte[] template) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("delete from huella where codigo_empleado = ? ");
        stmt.setString(1, code);
        stmt.executeUpdate();
        stmt = c.prepareStatement("insert into huella (codigo_empleado,imagen) values(?,?)");
        stmt.setString(1, code);
        stmt.setBytes(2, template);
        stmt.executeUpdate();

        c.close();
    }

    static List<FingerPrint> getAllFingerPrints() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select codigo_empleado , imagen from huella");
        ResultSet rs = stmt.executeQuery();

        List<FingerPrint> ans = new LinkedList<FingerPrint>();

        while( rs.next() ){
            ans.add(new FingerPrint(rs.getString("codigo_empleado"), rs.getBytes("imagen")));
        }

        c.close();
        return ans;
    }

    static void deleteAllFreeDay(String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from inasistencia where datediff(? , fecha)=0");
        stmt.setString(1, myDay);
        stmt.executeUpdate();
        c.close();
    }

    static void deleteAllOverTime(String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("delete from horas_extra where datediff(? , fecha)=0");
        stmt.setString(1, myDay);
        stmt.executeUpdate();
        c.close();
    }

    static void createFreeDay(DefaultTableModel model, String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            /*String employCode = ((String) model.getValueAt(i, 0)).split("-")[0].trim() ;
            String concept = (String) model.getValueAt(i, 1) ;
            String hours = model.getValueAt(i, 2)+"" ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into inasistencia ( agencia , fecha, codigo_empleado, concepto ) values ( ? , ? , ? , ? )");
            stmt.setString(1, Shared.getConfig("department"));
            stmt.setString(2, day);
            stmt.setString(3, employCode);
            stmt.setString(4, concept);
            stmt.executeUpdate();
            stmt = c.prepareStatement(
                "insert into horas_extra ( agencia , fecha, codigo_empleado, cantidad_horas ) values ( ? , ? , ? , ? )");
            stmt.setString(1, Shared.getConfig("department"));
            stmt.setString(2, day);
            stmt.setString(3, employCode);
            stmt.setString(4, hours);
            stmt.executeUpdate();*/
            String employCode = ((String) model.getValueAt(i, 0)).split("-")[0].trim() ;
            String concept = (String) model.getValueAt(i, 1) ;
            String hours = model.getValueAt(i, 2)+"" ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into inasistencia ( fecha, codigo_empleado, concepto , fecha_cambio) values ( ? , ? , ? , now() )");
            if ( !concept.equals(" ") || !hours.equals("0") ){
                stmt.setString(1, day);
                stmt.setString(2, employCode);
                stmt.setString(3, concept);
                stmt.executeUpdate();
                
                stmt = c.prepareStatement(
                    "insert into horas_extra ( fecha_cambio , fecha, codigo_empleado, cantidad_horas ) values ( now() , ? , ? , ? )");
                stmt.setString(1, day);
                stmt.setString(2, employCode);
                stmt.setString(3, hours);
                stmt.executeUpdate();
            }
        }
        c.close();
    }

    /*static void createOverTime(DefaultTableModel model, String day) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        for (int i = 0; i < model.getRowCount(); i++) {
            String employCode = ((String) model.getValueAt(i, 0)).split("-")[0].trim() ;
            String hours = model.getValueAt(i, 1)+"" ;
            PreparedStatement stmt = c.prepareStatement(
                "insert into horas_extra ( agencia , fecha, codigo_empleado, cantidad_horas ) values ( ? , ? , ? , ? )");
            stmt.setString(1, Shared.getConfig("department"));
            stmt.setString(2, day);
            stmt.setString(3, employCode);
            stmt.setString(4, hours);
            stmt.executeUpdate();
        }
        c.close();
    }*/

    static List<FreeDay> listAllFreeDays(String myDay) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select horas_extra.codigo_empleado , concepto, horas_extra.cantidad_horas from inasistencia, horas_extra where datediff(horas_extra.fecha,?)=0 and horas_extra.codigo_empleado = inasistencia.codigo_empleado and horas_extra.fecha=inasistencia.fecha");
        stmt.setString(1,myDay);
        ResultSet rs = stmt.executeQuery();

        List<FreeDay> ans = new LinkedList<FreeDay>();

        while( rs.next() ){
            ans.add(new FreeDay(ConnectionDrivers.getEmploy(rs.getString("codigo_empleado")), rs.getString("concepto"), rs.getString("cantidad_horas")));
        }

        c.close();
        return ans;
    }

    static List<OverTime> listAllOverTime(String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select codigo_empleado , cantidad_horas from horas_extra , empleado where datediff(fecha,?)=0 and empleado.codigo = horas.codigo_empleado and empleado.departamento like ? ");
        stmt.setString(1,myDay);
        stmt.setString(2, Shared.getConfig("storeName"));
        ResultSet rs = stmt.executeQuery();

        List<OverTime> ans = new LinkedList<OverTime>();

        while( rs.next() ){
            ans.add(new OverTime(ConnectionDrivers.getEmploy(rs.getString("codigo_empleado")), rs.getInt("cantidad_horas")));
        }

        c.close();
        return ans;
    }

    static String saveFingerPrint(Employ e) throws SQLException, Exception{
        Presence p = listAllFingerPrintMarks("curdate()", e);

        String t = ConnectionDrivers.getLastMark(e);

        if ( t != null && Constants.halfHour.compareTo(t) >= 0 && p.getFingerPrints().size() < 4 ){
            return Constants.fingerPrintRepeated;
        }

        Connection c = ConnectionDrivers.cpds.getConnection();

        if ( p.getFingerPrints().isEmpty() ){

            PreparedStatement stmt = c.prepareStatement("insert into marcacion ( agencia, codigo_empleado , hora ) values( ? , ? , now() )");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();

            stmt = c.prepareStatement("insert into asistencia ( agencia , fecha , codigo_empleado , marcacion1 ) values(?,curdate(),?,now())");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
        }else if ( p.getFingerPrints().size() == 1){
            PreparedStatement stmt = c.prepareStatement("insert into marcacion ( agencia, codigo_empleado , hora ) values( ? , ? , now() )");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();

            stmt = c.prepareStatement("update asistencia set marcacion2=now() where agencia=? and fecha=curdate() and codigo_empleado=?");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
        }else if ( p.getFingerPrints().size() == 2){
            PreparedStatement stmt = c.prepareStatement("insert into marcacion ( agencia, codigo_empleado , hora ) values( ? , ? , now() )");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
            stmt = c.prepareStatement("update asistencia set marcacion3=now() where agencia=? and fecha=curdate() and codigo_empleado=?");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
        }else if ( p.getFingerPrints().size() == 3 ){
            PreparedStatement stmt = c.prepareStatement("insert into marcacion ( agencia, codigo_empleado , hora ) values( ? , ? , now() )");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();

            stmt = c.prepareStatement("update asistencia set marcacion4=now() where agencia=? and fecha=curdate() and codigo_empleado=?");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
        }else{
            deleteLastFingerPrint(e, "curdate()");
            PreparedStatement stmt = c.prepareStatement("insert into marcacion ( agencia, codigo_empleado , hora ) values( ? , ? , now() )");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
            stmt = c.prepareStatement("update asistencia set marcacion4=now() where agencia=? and fecha=curdate() and codigo_empleado=?");
            stmt.setString(1, Shared.getConfig("storeName"));
            stmt.setString(2, e.getCode());
            stmt.executeUpdate();
        }

        c.close();

        return "ACEPTADO";
    }

    static String currentHour() throws SQLException{
        //SELECT DATE_FORMAT(now(),'%r')
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("SELECT DATE_FORMAT(now(),'%r') as hora");
        ResultSet rs = stmt.executeQuery();

        String tmp = "";

        if ( rs.next() ){
            tmp = rs.getString("hora");
        }
        c.close();
        return tmp;
    }

    static boolean isBeforeHalfDay() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select mid(now(),12,9) as hora");
        ResultSet rs = stmt.executeQuery();

        String tmp = "";

        if ( rs.next() ){
            tmp = rs.getString("hora");
        }
        c.close();
        return tmp.compareTo(Constants.halfDay) < 0;
    }

    static void deleteLastFingerPrint(Employ e, String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select max(hora) as hora from marcacion where agencia=? and datediff("+ myDay +",hora)=0 and codigo_empleado=?");
        stmt.setString(1,Shared.getConfig("storeName"));
        stmt.setString(2, e.getCode());
        ResultSet rs = stmt.executeQuery();

        String tTime = "";
        if ( rs.next() ){
            tTime = rs.getString("hora");
        }

        stmt = c.prepareStatement("delete from marcacion where codigo_empleado=? and hora = ?");
        stmt.setString(1, e.getCode());
        stmt.setString(2, tTime);
        stmt.executeUpdate();

        c.close();
    }

    static List<Presence4Print> listAllPresence4Print(String myDay) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        List<Presence4Print> ans = new LinkedList<Presence4Print>();

        PreparedStatement stmt = c.prepareStatement("select codigo_empleado ,DATE_FORMAT( marcacion1 ,'%r') as marcacion1  , DATE_FORMAT(marcacion2,'%r') as marcacion2 , DATE_FORMAT(marcacion3,'%r') as marcacion3, DATE_FORMAT(marcacion4,'%r') as marcacion4 from asistencia where datediff(fecha,?)=0");
        stmt.setString(1,myDay);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            ans.add(new Presence4Print(ConnectionDrivers.getEmploy(rs.getString("codigo_empleado")), rs.getString("marcacion1"), rs.getString("marcacion2"), rs.getString("marcacion3"), rs.getString("marcacion4")));
        }

        c.close();
        return ans;
    }

    static Presence listAllFingerPrintMarks(String myDay, Employ e) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select codigo_empleado, hora from marcacion where agencia=? and datediff("+ myDay +",hora)=0 and codigo_empleado=? order by hora");
        stmt.setString(1,Shared.getConfig("storeName"));
        stmt.setString(2, e.getCode());
        ResultSet rs = stmt.executeQuery();

        Presence ans = new Presence(e);

        while( rs.next() ){
            ans.addFingerPrint(rs.getTimestamp("hora"));
        }

        c.close();
        return ans;
    }

    static String getLastMark(Employ e) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select hora from marcacion where hora=(select max(hora) from marcacion where codigo_empleado=?) and datediff(hora, curdate())=0");
        stmt.setString(1, e.getCode());
        ResultSet rs = stmt.executeQuery();

        boolean n = rs.next();

        if ( !n ){
            c.close();
            return null;
        }

        String tmp = rs.getString("hora");

        stmt = c.prepareStatement("select timediff(now() , ? ) as diffe");
        stmt.setString(1, tmp);
        rs = stmt.executeQuery();

        n = rs.next();
        assert( n );

        String ans = rs.getString("diffe");
        

        c.close();
        return ans;
    }

    public static void recalculateStock() throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("update articulo , existencia_desde_movimientos set articulo.existencia_actual = existencia_desde_movimientos.cantidad where articulo.codigo=existencia_desde_movimientos.codigo_de_articulo");
        stmt.executeUpdate();
        c.close();
    }

    public static List<Employ> getAllEmployBetween(String from, String until, String store) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();

        List<Employ> ans = new LinkedList<Employ>();

        PreparedStatement stmt = c.prepareStatement("select distinct codigo_empleado, nombre_completo from asistencia , empleado where ? <= fecha and fecha <= ? and empleado.codigo=asistencia.codigo_empleado order by codigo_empleado");
        stmt.setString(1, from);
        stmt.setString(2, until);

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            ans.add(new Employ(rs.getString("codigo_empleado"),rs.getString("nombre_completo")));
        }

        c.close();
        

        return ans;
    }

    static void calculatePresence(DefaultTableModel model, String from , String until, String store, Map<String, Integer> employRow, int offset) throws SQLException{
        System.out.println("Calculando presencia...");
        Connection c = ConnectionDrivers.cpds.getConnection();
        PreparedStatement stmt = c.prepareStatement("select codigo_empleado , datediff(fecha,?) as diff from asistencia, empleado where datediff(fecha, ?) >= 0 and datediff(fecha, ?)<=0 and empleado.codigo=asistencia.codigo_empleado and empleado.departamento like ?");
        stmt.setString(1, from);
        stmt.setString(2, from);
        stmt.setString(3, until);
        stmt.setString(4, store);

        ResultSet rs = stmt.executeQuery();

        while ( rs.next() ){
            model.setValueAt("S", employRow.get(rs.getString("codigo_empleado")), offset + rs.getInt("diff"));
            System.out.println("Empleado " + employRow.get(rs.getString("codigo_empleado")) + " "  + ( offset + rs.getInt("diff")));
        }

        stmt = c.prepareStatement("select datediff(fecha,?) as diff , codigo_empleado, concepto from inasistencia , empleado where concepto!=' ' and  datediff(fecha, ?) >= 0 and datediff(fecha, ?)<=0 and inasistencia.codigo_empleado=empleado.codigo and empleado.departamento like ?");
        stmt.setString(2, from);
        stmt.setString(3, until);
        stmt.setString(4, store);
        stmt.setString(1, from);
        rs = stmt.executeQuery();
        while ( rs.next() ){
            model.setValueAt(rs.getString("concepto"), employRow.get(rs.getString("codigo_empleado")), offset + rs.getInt("diff"));
        }

        //TODO QUE OCURRE ACA!
        /*for ( int i = 0 ; i < model.getRowCount() ; i++ ){
            model.setValueAt("0", i, 2);
        }*/
        
        c.close();
    }

    static void calculateExtraHours(DefaultTableModel model, String from , String until, String store, Map<String, Integer> employRow, int offset) throws SQLException {
        for ( int i = 0 ; i < model.getRowCount() ; i++ ){
            model.setValueAt("0", i, 2);
        }

        Connection c = ConnectionDrivers.cpds.getConnection();

        PreparedStatement stmt = c.prepareStatement("select codigo_empleado, sum(cantidad_horas) as sch from horas_extra, empleado where datediff(fecha, ?) >= 0 and datediff(fecha, ?)<=0 and horas_extra.codigo_empleado=empleado.codigo and empleado.departamento like ? group by codigo_empleado having sch > 0");
        stmt.setString(1, from);
        stmt.setString(2, until);
        stmt.setString(3, store);
        ResultSet rs = stmt.executeQuery();
        while ( rs.next() ){
            model.setValueAt(rs.getString("sch"), employRow.get(rs.getString("codigo_empleado")), 2);
        }
        stmt = c.prepareStatement("select codigo_empleado, mid(concat(sec_to_time(sum(secondsbyday)),''),1,6) as extrahours from (select codigo_empleado , Time_to_Sec(timediff(max(t),?)) as secondsbyday, datediff(fecha,?) as diff from (select codigo_empleado, timediff(marcacion4, marcacion1) as t, fecha from asistencia having t is not NULL union select codigo_empleado, timediff(marcacion3, marcacion1) as t, fecha from asistencia having t is not NULL union select codigo_empleado, timediff(marcacion2, marcacion1) as t, fecha from asistencia having t is not NULL) as t where datediff(fecha, ?) >= 0 and datediff(fecha, ?)<=0 group by codigo_empleado , fecha ) as sbt , empleado where sbt.codigo_empleado=empleado.codigo and empleado.departamento like ? group by codigo_empleado");
        stmt.setString(1, Constants.workingHours);
        stmt.setString(2, from);
        stmt.setString(3, from);
        stmt.setString(4, until);
        stmt.setString(5, store);
        rs = stmt.executeQuery();
        while ( rs.next() ){
            model.setValueAt(rs.getString("extrahours"), employRow.get(rs.getString("codigo_empleado")), 3);
        }

        stmt = c.prepareStatement("select codigo_empleado, time(hora) as th, TIME_TO_SEC(timediff(time(hora),?)) as diff from marcacion, empleado where datediff(hora, ?) >= 0 and datediff(hora, ?)<=0 and empleado.codigo=marcacion.codigo_empleado and empleado.departamento like ? having th > ?");
        stmt.setString(5, Constants.beginOfNightBonus);
        stmt.setString(1, Constants.beginOfNightBonus);
        stmt.setString(2, from);
        stmt.setString(3, until);
        stmt.setString(4, store);
        rs = stmt.executeQuery();
        while ( rs.next() ){
            int m = rs.getInt("diff")/60+15;
            double an = (m/60) + (m%60>=45?.5:.0);
            model.setValueAt(an, employRow.get(rs.getString("codigo_empleado")), 4);
        }

        for (int i = 0; i < model.getRowCount(); i++) {

            boolean isOk = true;
            for (int j = offset; j < model.getColumnCount() && isOk ; j++) {
                if ( model.getValueAt(i, j) == null || !Shared.didItCome(model.getValueAt(i, j).toString() ) ){
                    isOk = false;
                }
            }

            if ( isOk ){
                model.setValueAt(Shared.getConfig("presenceBonus"), i, 5);
            }else{
                model.setValueAt(.0, i, 5);
            }
        }

        stmt = c.prepareStatement("select codigo_empleado, count(*) as leves from "
                + "(select marcacion.codigo_empleado , (count(*)) as marcaciones from asistencia "
                + ", marcacion, empleado where marcacion.codigo_empleado=asistencia.codigo_empleado"
                + " and marcacion.codigo_empleado=empleado.codigo and ? <= fecha and fecha <= ?"
                + " and datediff(fecha, hora) =0 group by marcacion.agencia, fecha , marcacion.codigo_empleado"
                + " having marcaciones=3 order by nombre_completo, fecha) as myTable, empleado where "
                + "myTable.codigo_empleado=empleado.codigo and empleado.departamento like ? group by codigo_empleado");
        stmt.setString(1, from);
        stmt.setString(2, until);
        stmt.setString(3, store);
        rs = stmt.executeQuery();

        while ( rs.next() ){
            model.setValueAt(rs.getString("leves"), employRow.get(rs.getString("codigo_empleado")), 7);
        }

        stmt = c.prepareStatement("select codigo_empleado, count(*) as leves from "
                + "(select marcacion.codigo_empleado , (count(*)) as marcaciones from asistencia "
                + ", marcacion, empleado where marcacion.codigo_empleado=asistencia.codigo_empleado"
                + " and marcacion.codigo_empleado=empleado.codigo and ? <= fecha and fecha <= ?"
                + " and datediff(fecha, hora) =0 group by marcacion.agencia, fecha , marcacion.codigo_empleado"
                + " having marcaciones=2 or marcaciones=1 order by nombre_completo, fecha) as myTable, empleado where "
                + "myTable.codigo_empleado=empleado.codigo and empleado.departamento like ? group by codigo_empleado");
        stmt.setString(1, from);
        stmt.setString(2, until);
        stmt.setString(3, store);
        rs = stmt.executeQuery();

        while ( rs.next() ){
            model.setValueAt(rs.getString("leves"), employRow.get(rs.getString("codigo_empleado")), 8);
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            int soft = 0;
            if ( model.getValueAt(i, 7) != null ){
                soft = Integer.parseInt(model.getValueAt(i, 7).toString());
            }
            int hard = 0;
            if ( model.getValueAt(i, 8) != null ){
                hard = Integer.parseInt(model.getValueAt(i, 8).toString());
            }

            model.setValueAt(soft/4+hard, i, 9);
        }
        
        c.close();
    }

    static void saveTable(DefaultTableModel model, String fromDate, String untilDate, String storeName, int offset, boolean isCestatickets) throws SQLException, ParseException, Exception {

        Connection c = ConnectionDrivers.cpds.getConnection();
        try{

            c.setAutoCommit(false);

            PreparedStatement stmt;
            if ( !isCestatickets ){
                stmt = c.prepareStatement("delete from cuadro_recursos_humanos where fecha_inicio = ? and fecha_fin = ? and agencia = ? ");

                stmt.setString(1, fromDate);
                stmt.setString(2, untilDate);
                stmt.setString(3, storeName);
                stmt.executeUpdate();


                for (int i = 0; i < model.getRowCount(); i++) {

                    java.util.Date fromDateD = Constants.sdfDay2DB.parse(fromDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fromDateD);

                    for (int j = 0; j < model.getColumnCount() && j < offset; j++) {
                        if (  model.getValueAt(i, j) != null ){
                            stmt = c.prepareStatement("insert into cuadro_recursos_humanos (fecha_inicio, fecha_fin, agencia, x, y, v) values (?,?,?,?,?,?)");
                            stmt.setString(1, fromDate);
                            stmt.setString(2, untilDate);
                            stmt.setString(3, storeName);
                            stmt.setString(4, i+"");
                            stmt.setString(5, j+"");
                            stmt.setString(6, model.getValueAt(i, j).toString());
                            stmt.executeUpdate();
                        }
                    }

                }
            }

            for (int i = 0; i < model.getRowCount(); i++) {

                java.util.Date fromDateD = Constants.sdfDay2DB.parse(fromDate);
                Calendar cal = Calendar.getInstance();
                cal.setTime(fromDateD);
                for ( int j = offset ; j < model.getColumnCount() ; j++ ){
                    stmt = c.prepareStatement("insert ignore into asistencia_calculada (agencia , fecha) values (?,?)");
                    stmt.setString(1, storeName);

                    java.sql.Date da = new Date(fromDateD.getTime());
                    stmt.setDate(2, da);
                    stmt.executeUpdate();

                    stmt = c.prepareStatement("delete from asistencia_final where datediff(fecha, ?)=0 and codigo_empleado = ?");
                    stmt.setDate(1, da);
                    stmt.setString(2, model.getValueAt(i, 0).toString());
                    stmt.executeUpdate();

                    stmt = c.prepareStatement("insert into asistencia_final(fecha,codigo_empleado,concepto) values(?,?,?)");
                    stmt.setDate(1, da);
                    stmt.setString(2, model.getValueAt(i, 0).toString());
                    if ( model.getValueAt(i, j) == null ){
                        stmt.setString(3, "");
                    }else{
                        stmt.setString(3, model.getValueAt(i, j).toString());
                    }
                    stmt.executeUpdate();

                    cal.add(Calendar.DATE, 1);
                    fromDateD = cal.getTime();
                    cal.setTime(fromDateD);
                }
            }

            c.commit();
        }catch(Exception ex){
            System.out.println("AGARRADO... ROLLINGBACK...");
            c.rollback();
            c.setAutoCommit(true);
            c.close();
            throw ex;
        }
        
        c.setAutoCommit(true);
        c.close();
    }

    static boolean loadHours( JTable presenceTable ,String fromDateString, String untilDateString, String storeName, java.util.Date fromDate, java.util.Date untilDate , AnalizePresence ap) throws SQLException {
        Connection c = ConnectionDrivers.cpds.getConnection();
        boolean ans = false;

        DefaultTableModel model = (DefaultTableModel) presenceTable.getModel();

        PreparedStatement stmt = c.prepareStatement("select x , y , v  from cuadro_recursos_humanos where fecha_inicio = ? and fecha_fin = ? and agencia = ? ");
        stmt.setString(1, fromDateString);
        stmt.setString(2, untilDateString);
        stmt.setString(3, storeName);
        ResultSet rs = stmt.executeQuery();

        while( rs.next() ){
            ans = true;
            model.setValueAt(rs.getString("v"), rs.getInt("x"), rs.getInt("y"));
            if ( rs.getInt("y") == 0 ){
                ap.map4Employs.put(rs.getString("v"), rs.getInt("x"));
            }
        }

        c.close();
        return ans;
    }

    protected static boolean loadPresence( JTable presenceTable ,String fromDateString, String untilDateString, String storeName, java.util.Date fromDate, java.util.Date untilDate , AnalizePresence ap, Map<String, Integer> map4employes, int offset) throws SQLException{
        Connection c = ConnectionDrivers.cpds.getConnection();
        boolean ans = false;

        PreparedStatement stmt = c.prepareStatement("select count(*) as n from asistencia_calculada where agencia=? and ? <= fecha and fecha <= ?");
        stmt.setString(2, fromDateString);
        stmt.setString(3, untilDateString);
        stmt.setString(1, storeName);
        ResultSet rs = stmt.executeQuery();

        boolean t = rs.next();
        assert(t);
            
        int n1 = rs.getInt("n");

        stmt = c.prepareStatement("select datediff(?, ?)+1 as n");
        stmt.setString(2, fromDateString);
        stmt.setString(1, untilDateString);
        rs = stmt.executeQuery();

        t = rs.next();
        assert(t);

        int n2 = rs.getInt("n");
        System.out.println("N1 = " + n1 + " , N2 = " + n2 );
        ans = (n1 == n2);

        if ( ans ){

            DefaultTableModel model = (DefaultTableModel) presenceTable.getModel();
            stmt = c.prepareStatement("select datediff(fecha,?) as diff, codigo_empleado, concepto from asistencia_final where ? <= fecha and fecha <= ? order by codigo_empleado , diff");
            stmt.setString(1, fromDateString);
            stmt.setString(2, fromDateString);
            stmt.setString(3, untilDateString);
            rs = stmt.executeQuery();

            while( rs.next() ){
                System.out.println("Concepto = " + rs.getString("concepto") + " Empleado " + map4employes.get(rs.getString("codigo_empleado")) + " ("+rs.getString("codigo_empleado")+") " + (rs.getInt("diff")+offset) );
                model.setValueAt(rs.getString("concepto"), map4employes.get(rs.getString("codigo_empleado")), rs.getInt("diff")+offset);
            }
        }

        c.close();
        return ans;
    }

    protected static void clearBonuses(Connection c) throws SQLException{
        PreparedStatement stmt = c.prepareStatement("update snem_va set val_n= 0 ");
        stmt.executeUpdate();
    }

    @Deprecated
    protected static void addExtraHour(double hours, String employId, Connection c) throws SQLException{

        PreparedStatement stmt = c.prepareStatement("update snem_va set val_n= ? , co_us_mo = ? , fe_us_mo = getdate() WHERE (cod_emp = ?) AND (co_var = ?)");
        stmt.setDouble(1, hours);
        stmt.setString(2, "TPOS");
        stmt.setString(3, employId);
        stmt.setString(4, "B001");
        int n = stmt.executeUpdate();

        if ( n == 0 ){
            
            System.out.println("WARNING...Horas extras no agregada al trabajador " + employId);
            /*
            stmt = c.prepareStatement("insert into snem_va ( co_var, cod_emp, val_n, co_gru, co_us_in, fe_us_in, co_us_mo, fe_us_mo ) "
                    + "values( ? , ? , ? , ? , ? , getdate() , ? , getdate() )");
            stmt.setString(1, "B001");
            stmt.setString(2, employId);
            stmt.setDouble(3, hours);
            stmt.setString(4, "ASG_EXT");
            stmt.setString(5, "TPOS");
            stmt.setString(6, "TPOS");
            stmt.executeUpdate();*/
        }
    }

    @Deprecated
    protected static void addSpecialBonus(double hours, String employId, Connection c) throws SQLException{

        PreparedStatement stmt = c.prepareStatement("update snem_va set val_n= ? , co_us_mo = ? , fe_us_mo = getdate() WHERE (cod_emp = ?) AND (co_var = ?)");
        stmt.setDouble(1, hours);
        stmt.setString(2, "TPOS");
        stmt.setString(3, employId);
        stmt.setString(4, "A313");
        int n = stmt.executeUpdate();

        if ( n == 0 ){
            System.out.println("WARNING...Bono especial no agregado al trabajador " + employId);
            /*stmt = c.prepareStatement("insert into snem_va ( co_var, cod_emp, val_n, co_us_in, fe_us_in, co_us_mo, fe_us_mo ) "
                    + "values( ? , ? , ? , ? , getdate() , ? , getdate() )");
            stmt.setString(1, "A313");
            stmt.setString(2, employId);
            stmt.setDouble(3, hours);
            stmt.setString(4, "TPOS");
            stmt.setString(5, "TPOS");
            stmt.executeUpdate();*/
        }
    }

    @Deprecated
    protected static void addNightBonus(double hours, String employId, Connection c) throws SQLException{

        PreparedStatement stmt = c.prepareStatement("update snem_va set val_n= ? , co_us_mo = ? , fe_us_mo = getdate() WHERE (cod_emp = ?) AND (co_var = ?)");
        stmt.setDouble(1, hours);
        stmt.setString(2, "TPOS");
        stmt.setString(3, employId);
        stmt.setString(4, "B003");
        int n = stmt.executeUpdate();

        if ( n == 0 ){
            System.out.println("WARNING...Bono nocturno no agregado al trabajador " + employId);
            /*stmt = c.prepareStatement("insert into snem_va ( co_var, cod_emp, val_n, co_us_in, fe_us_in, co_us_mo, fe_us_mo ) "
                    + "values( ? , ? , ? , ? , getdate() , ? , getdate() )");
            stmt.setString(1, "B003");
            stmt.setString(2, employId);
            stmt.setDouble(3, hours);
            stmt.setString(4, "TPOS");
            stmt.setString(5, "TPOS");
            stmt.executeUpdate();*/
        }
    }

    protected static void addBonus(double hours, String employId, String concept, Connection c, String comment) throws SQLException{

        PreparedStatement stmt = c.prepareStatement("update snem_va set val_n= ? , co_us_mo = ? , fe_us_mo = getdate(), comentario = ? WHERE (cod_emp = ?) AND (co_var = ?)");
        stmt.setDouble(1, hours);
        stmt.setString(2, "TPOS");
        stmt.setString(3, comment);
        stmt.setString(4, employId);
        stmt.setString(5, concept);
        stmt.executeUpdate();
    }

    protected static void lockClosingDay(Connection c) throws SQLException{
        PreparedStatement stmt = c.prepareStatement("lock table dia_operativo write");
        stmt.executeUpdate();
    }

    protected static void unlock(Connection c) throws SQLException{
        PreparedStatement stmt = c.prepareStatement("unlock tables");
        stmt.executeUpdate();
    }

}
