<?php

// подключаем db_connect класс
require_once("../Db/BaseConnect.php");
require_once("../Process/UserShare.php");

(new Process\UserShare())->run($_POST);
