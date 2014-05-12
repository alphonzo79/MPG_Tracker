<?php
/**
 * Created by IntelliJ IDEA.
 * User: joe
 * Date: 5/10/14
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */

function connect() {
    $con = mysql_connect(DB_HOST, DB_USER, DB_PASS);
    mysql_select_db(DB_DBNAME, $con);
}

function authenticate($client, $secret) {
    $client_clean = mysql_real_escape_string($client);
    $secret_clean = mysql_real_escape_string($secret);
    $sql = "SELECT * FROM users WHERE client='".$client_clean."' AND secret='".$secret_clean."'";
    echo $sql."</br>";
    $result = mysql_query($sql);

    if($result) {
        $row = mysql_fetch_array($result);
        if($row && $row[0] == $client && $row[1] == $secret) {
            return true;
        } else {
            return false;
        }
    }
}

?>