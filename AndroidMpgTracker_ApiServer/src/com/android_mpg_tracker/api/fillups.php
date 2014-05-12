<?php
/**
 * Created by IntelliJ IDEA.
 * User: joe
 * Date: 5/11/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

include_once '../includes/db_funcs.inc';
require_once "RestBase.php";

$mysql = connect_to_db();
$rest_obj = new RestBase($mysql);
if($rest_obj->validate_request()) {
    $method = $_GET['method'];
    if(empty($method)) {
        $method = $_POST['method'];
    }

    if($method == 'getCommunityMpg') {
        $result = get_community_mpg();
        $rest_obj->prepare_and_send_response("Success", $result);
    } else if($method == 'logMpg') {
        if(log_mpg()) {
            $rest_obj->prepare_and_send_response("Success", true);
        } else {
            $rest_obj->prepare_and_send_response("UnknownError", false);
        }
    } else {
        $rest_obj->prepare_and_send_response("BadRequest", null);
    }
}

function get_community_mpg() {
    return -1;
    //todo
}

function log_mpg() {
    return false;
    //todo
}

?>