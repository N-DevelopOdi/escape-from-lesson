<?php

// подключаем db_connect класс
require_once("../Db/BaseConnect.php");
require_once("../Process/RegistrationUser.php");

(new Process\RegistrationUser())->run($_POST);
