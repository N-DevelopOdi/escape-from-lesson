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

class UserShare implements Process
{
	/**
	 * @var BaseConnect
	 */
	protected $connect;

	private  $__table = "users";

	public function run($data)
	{
		$data['user'] = "1234";
		$data['share'] = 3;


		$this->connect = new BaseConnect();
		try
		{
			$response["success"] = 0;

			if(isset($data['share']) && $data['share'])
			{

				$is_user = $this->connect->select($this->__table, ["user" => $data['user']])->fetch();

				if ($is_user)
				{
					// плюсовать
					$this->connect->query("UPDATE " . $this->__table .
						" SET share = share + " . $data['share'] .
						" WHERE user = " . $data['user']);
				}
				else
				{
					$this->connect->insert($this->__table, $data);
				}

				$response["success"] = 1;
			}
		}
		catch (Exception $e)
		{
			$response["success"] = 0;

			// echo no users JSON
		}
		echo json_encode($response);
	}
}