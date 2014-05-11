<?php
/**
 * Created by IntelliJ IDEA.
 * User: joe
 * Date: 5/10/14
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */

function authenticate($db, $client, $secret) {
    $sql = "SELECT * FROM users WHERE client=? AND secret=?";
    $stmt = $db->prepare($sql);
    $stmt->execute(array($client, $secret));
}

?>