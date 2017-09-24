<?php

require_once("db_config.php");


/**
 * A class file to connect to database
 */
class BaseConnect
{
    // constructor
    function __construct() 
	{
        // connecting to database
        $this->__connect();
    }
 
    // destructor
    function __destruct()
    {
	    // closing db connection
	    $this->close();
    }
 
    /**
     * Function to connect with database
     */
    private function __connect()
	{
        // import database connection variables
//        require_once __DIR__ . '/db_config.php';
 
        // Connecting to mysql database
//        $con = new PDO('mysql:host=' . DB_SERVER . ';dbname=' . DB_DATABASE, DB_USER, DB_PASSWORD);
        $con = new PDO('mysql:host=' . DB_SERVER . ';dbname=' . DB_DATABASE, DB_USER, DB_PASSWORD);

        // Selecing database
//        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
 
        // returing connection cursor
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    function close() 
	{
        // closing db connection
//        $con ;
    }

	/**
	 * @param       $sql
	 * @param array $bind
	 * @return \PDOStatement
	 */
	public function query($sql, array $bind = array())
	{
//		assert('$this->_assertReadOnly($sql)', 'Нельзя изменять данные через адаптер "только чтение"');

		$connect = $this->__connect();
		$stmt = $connect->prepare($sql);

		try
		{
			$stmt->execute($bind);
			return $stmt;
		}
		catch (Exception $e)
		{
			return $e->getMessage();
		}
	}

	public function insert($table, array $data)
	{
		$countData = count($data);

		if ($countData > 0)
		{
			$values = [];
			$values_fill = [];
			foreach ($data as $key => $val)
			{
				if ($val instanceof Db\Expression)
				{
					$values_fill[] = $val->getValue();
				}
				else
				{
					$values_fill[] = '?';
					$values[] = $val;
				}
			}

			$sql = 'INSERT INTO ' . $table . ' (' . implode(', ', array_keys($data)) . ')
					VALUES (' . implode(', ', $values_fill) . ')';

			return $this->query($sql, $values);
		}
	}

	public function insertOrUpdate($table, array $data, $auto_increment_id = NULL)
	{
		$countData = count($data);

		if ($countData > 0)
		{
			$sql = '';
			$values = [];
			$values_fill = [];

			if ($auto_increment_id)
			{
				$sql .= ' '.$auto_increment_id.'=LAST_INSERT_ID('.$auto_increment_id.') ';
			}

			foreach ($data as $key => $val)
			{
				empty($sql) || $sql .= ',';

				$sql .= "$key = VALUES($key)";

				if ($val instanceof Db\Expression)
				{
					$values_fill[] = $val->getValue();
				}
				else
				{
					$values_fill[] = '?';
					$values[] = $val;
				}
			}

			$sql = 'INSERT INTO ' . $table . ' (' . implode(', ', array_keys($data)) . ')
					VALUES (' . implode(', ', $values_fill) . ')
					ON DUPLICATE KEY UPDATE
					'.$sql;

			return $this->query($sql, $values);
		}
	}

	public function update($table, array $data, array $where)
	{
		if (count($data) && $where)
		{
			$fields = [];
			$values = [];
			foreach ($data as $key => $val)
			{
				if ($val instanceof Db\Expression)
				{
					$fields[] = $key . ' = ' . $val->getValue();
				}
				else
				{
					$fields[] = $key . ' = ?';
					$values[] = $val;
				}
			}

			foreach ($where as $key => $value)
			{
				if (is_array($value))
				{
					unset($where[$key]);

					$columns[] = $key . ' IN (' . $this->quoteIn($value) . ')';
				}
				else
				{
					$columns[] = $key . ' = ?';
				}
			}

			$sql = 'UPDATE ' . $table . '
					SET   ' . implode(', ', $fields) . '
					WHERE ' . implode(' AND ', $columns);

			return $this->query($sql, array_merge($values, array_values($where)));
		}
	}

	public function select($table, array $where, array $fields = null, $order = null)
	{
		$whereString = '';

		if ($where)
		{
			$columns = array();

			foreach ($where as $key => $value)
			{
				if (is_array($value))
				{
					unset($where[$key]);

					$columns[] = $key . ' IN (' . $this->quoteIn($value) . ')';
				}
				else
				{
					$columns[] = $key . ' = ?';
				}
			}

			$whereString = ' WHERE ' . implode(' AND ', $columns);
		}

		$sql   = 'SELECT ' . ($fields ? implode(', ', $fields) : ' * ') . ' FROM ' . $table . $whereString;

		if ($order)
		{
			is_array($order)
				? $sql .= ' ORDER BY '.implode(',', $order)
				: $sql .= ' ORDER BY '.$order;
		}

		$bind  = $where ? array_values($where) : array();

		return $this->query($sql, $bind);
	}

	public function quoteIn(array $items)
	{
		return implode(', ', array_map(function($item)
			{
				if ($this->_is_int($item))
				{
					return $item;
				}

				if ($item instanceof Db\Expression)
				{
					return $item;
				}

				return '"' . addslashes($item) . '"';
			},
				$items
			)
		);
	}

	private function _is_int($value)
	{
		return ctype_digit(strval($value));
	}
 
}