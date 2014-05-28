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
        $result = get_community_mpg($mysql);
        if($result) {
            $rest_obj->prepare_and_send_response("Success", $result);
        } else {
            $rest_obj->prepare_and_send_response("UnknownError", $result);
        }
    } else if($method == 'logMpg') {
        $result = log_mpg($mysql);
        if($result) {
            $rest_obj->prepare_and_send_response("Success", $result);
        } else {
            $rest_obj->prepare_and_send_response("UnknownError", $result);
        }
    } else {
        $rest_obj->prepare_and_send_response("BadRequest", null);
    }
}

function get_community_mpg($mysql) {
    $ret_val = 0;

    $car_id = $_GET["car_id"];
    $car_id_clean = mysql_real_escape_string($car_id, $mysql);

    $count = 0;

    if(!empty($car_id_clean))  {
        //first find out how many so we can filter out outliers (highest and lowest 10%)
        $count_result = mysql_query("SELECT count(*) FROM fill_ups WHERE car_id=".$car_id_clean.";", $mysql);
        if($count_result) {
            $row = mysql_fetch_array($count_result, MYSQL_NUM);
            $count = $row[0];
            $ten_percent = (int)($count / 10);
            $count = $count - ($ten_percent * 2);

            if($count > 0) {
                //Now get results starting after the first 10% and ending before the last 10%
                $entries = mysql_query("SELECT mpg FROM fill_ups WHERE car_id=".$car_id_clean." ORDER BY mpg ASC LIMIT ".$count." OFFSET ".$ten_percent.";", $mysql);
                $total = 0;
                if($entries) {
                    while($row = mysql_fetch_array($entries)) {
                        $count++;
                        $total += $row[0];
                    }
                }

                if($count > 0 && $total > 0) {
                    $ret_val = $total / $count;
                }
            }
        }
    }

    return array("data" => array("count" => count($count), "mpg" => $ret_val));
}

function log_mpg($mysql) {
    $ret_val = false;

    $car_id = $_POST["car_id"];
    $mpg = $_POST["mpg"];

    $car_id_clean = mysql_real_escape_string($car_id, $mysql);
    $mpg_clean = mysql_real_escape_string($mpg, $mysql);

    if(!empty($car_id_clean) && !empty($mpg_clean)) {
        $ret_val = mysql_query("INSERT INTO fill_ups (car_id, mpg) VALUES (".$car_id_clean.",".$mpg_clean.")", $mysql);
    }

    return array("data" => $ret_val);
}