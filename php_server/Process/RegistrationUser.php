<?php
/**
 * Created by PhpStorm.
 * User: Dima
 * Date: 09.09.2017
 * Time: 19:37
 */

namespace Process;

require_once("../Interface/Process.php");
require_once("../Db/BaseConnect.php");
require_once("../Db/Exception.php");

use BaseConnect;
use Exception;
use Process;

class RegistrationUser implements Process
{
	/**
	 * @var BaseConnect
	 */
	protected $connect;

	private  $__table = "users";

	public function run($data)
	{
		$this->connect = new BaseConnect();
		try
		{
			$this->connect->insert($this->__table, $data);
			$response["success"] = 1;

		}
		catch (Exception $e)
		{
			$response["success"] = 0;

			// echo no users JSON
		}
		echo json_encode($response);
	}
}