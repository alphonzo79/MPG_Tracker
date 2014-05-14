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
        $rest_obj->prepare_and_send_response("Success", $result);
    } else if($method == 'logMpg') {
        if(log_mpg($mysql)) {
            $rest_obj->prepare_and_send_response("Success", true);
        } else {
            $rest_obj->prepare_and_send_response("UnknownError", false);
        }
    } else {
        $rest_obj->prepare_and_send_response("BadRequest", null);
    }
}

function get_community_mpg($mysql) {
    $ret_val = 0;

    $car_id = $_GET["car_id"];
    $car_id_clean = mysql_real_escape_string($car_id, $mysql);

    if(!empty($car_id_clean))  {
        //first find out how many so we can filter out outliers (highest and lowest 10%)
        $count_result = mysql_query("SELECT count(*) FROM fill_ups WHERE car_id=".$car_id_clean.";", $mysql);
        $count = 0;
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
                    $found_count = mysql_num_rows($entries);
                    while($row = mysql_fetch_array($entries)) {
                        $count++;
                        echo $row[0]."<br />";
                        $total += $row[0];
                    }
                }

                if($count > 0 && $total > 0) {
                    $ret_val = $total / $count;
                }
            }
        }
    }

    return $ret_val;
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

    return $ret_val;
}

?>