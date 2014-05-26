<?php
/**
 * Created by IntelliJ IDEA.
 * User: joe
 * Date: 5/10/14
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
include_once "db.inc";

function connect_to_db() {
    $con = mysql_connect(DB_HOST, DB_USER, DB_PASS);
    mysql_select_db(DB_DBNAME, $con);

    return $con;
}