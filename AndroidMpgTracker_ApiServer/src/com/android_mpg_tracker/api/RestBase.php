<?php
/**
 * Created by IntelliJ IDEA.
 * User: joe
 * Date: 5/11/14
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */

include_once "../includes/db_funcs.inc";

class RestBase {

    private $mysql;

    private $RESPONSE_STATUS = array("UnknownError" => array('HTTP Response' => 400, 'Message' => 'Unknown Error'),
        "Success" => array('HTTP Response' => 200, 'Message' => 'Success'),
        "NoHttps" => array('HTTP Response' => 403, 'Message' => 'HTTPS Required'),
        "AuthenticationRequired" => array('HTTP Response' => 401, 'Message' => 'Authentication Required. Please provide client and secret params.'),
        "AuthenticationFailed" => array('HTTP Response' => 401, 'Message' => 'Authentication Failed'),
        "BadRequest" => array('HTTP Response' => 400, 'Message' => 'Invalid Request. No method by that name in this service'),
        "MethodNotAllowed" => array('HTTP Response' => 405, 'Message' => 'Allow: GET, POST'));

    public function __construct($mysql) {
        $this->mysql = $mysql;
    }

    private function authenticate_client() {
        $client = $_POST['client'];
        $secret = $_POST['secret'];
        $result = false;

        $client_clean = mysql_real_escape_string($client, $this->mysql);
        $secret_clean = mysql_real_escape_string($secret, $this->mysql);
        if($client_clean && $secret_clean) {
            $sql = "SELECT * FROM users WHERE client='".$client_clean."' AND secret='".$secret_clean."'";
            $result = mysql_query($sql);
        }

        if($result) {
            $row = mysql_fetch_array($result);
            if($row && $row[0] == $client && $row[1] == $secret) {
                return true;
            }
        }

        return false;
    }

    function validate_request() {
        $result = false;
        if($_SERVER['REQUEST_METHOD'] != "GET" || $_SERVER['REQUEST_METHOD'] != "POST") {
            $this->prepare_and_send_response("MethodNotAllowed", null);
        } else if(empty($_POST['client']) || empty($_POST['secret'])) {
            $this->prepare_and_send_response("AthenticationRequired", null);
        } else {
            $result = $this->authenticate_client();
            if(!$result) {
                $this->prepare_and_send_response("AuthenticationFailed", null);
            }
        }

        return $result;
    }

    function prepare_and_send_response($response_type, $response_data) {
        $code_message = $this->RESPONSE_STATUS[$response_type];
        if($code_message == null) {
            $code_message = $this->RESPONSE_STATUS["UnknownError"];
        }
        header($code_message["Message"], true, $code_message["HTTP Response"]);
        header('Content-Type: application/json; charset=utf-8');

        $json_response = json_encode($response_data);

        // Deliver formatted data
        echo $json_response;
    }
}

?>
