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
        $entries = mysql_query("SELECT miles, gallons FROM fill_ups WHERE car_id=".$car_id_clean.";", $mysql);
        $total_miles = 0;
        $total_gallons = 0;
        if($entries) {
            while($row = mysql_fetch_array($entries)) {
                $count++;
                $total_miles += $row[0];
                $total_gallons += $row[1];
            }
        }

        if($total_gallons > 0 && $total_miles > 0) {
            $ret_val = $total_miles / $total_gallons;
        }
    }

    return array("data" => array("count" => $count, "mpg" => $ret_val));
}

function log_mpg($mysql) {
    $ret_val = false;

    $car_id = $_POST["car_id"];
    $miles = $_POST["miles"];
    $gallons = $_POST["gallons"];
    $price = $_POST["price"];

    $car_id_clean = mysql_real_escape_string($car_id, $mysql);
    $miles_clean = mysql_real_escape_string($miles, $mysql);
    $gallons_clean = mysql_real_escape_string($gallons, $mysql);
    $price_clean = mysql_real_escape_string($price, $mysql);

    if(!empty($car_id_clean) && !empty($miles_clean) && !empty($gallons_clean)) {
        $ret_val = mysql_query("INSERT INTO fill_ups (car_id, miles, gallons, price_per_gallon, date) VALUES (".$car_id_clean.",".$miles_clean.",".$gallons_clean.",".$price_clean.",".time().")", $mysql);
    }

    return array("data" => $ret_val);
}