<?php

namespace Db;

class Expression extends \Exception
{
	protected $_value;

	public function __construct($value)
	{
		$this->_value = $value;
	}

	public function __toString()
	{
		return $this->_value;
	}

	public function getValue()
	{
		return $this->_value;
	}

}