''' Specifically, X01-X06 are ocean-view rooms and X07-X12 are pool-view

rooms (X=1..5). '''

#!/usr/bin/python
import psycopg2
import sys
 
def insert_rooms():
	conn, cursor = db_connection()

	ocean_view_room_range = 6
	pool_view_room_range = 12
	floor_range = 5

	# insert ocean rooms
	for floor in range(floor_range):
		for room in range(ocean_view_room_range):
			room_number = (int(str(floor + 1) + str(room + 1)))

			if (room + 1) % 2 == 0:
				print("inserting", room_number)
				cursor.execute(
				     """INSERT INTO room (view, type, room_number)
				        VALUES (%s, %s, %s);""",
				    ("ocean", "double queen", room_number))
			else:
				print("inserting", room_number)
				cursor.execute(
				    """INSERT INTO room (view, type, room_number)
				        VALUES (%s, %s, %s);""",
				    ("ocean", "single king", room_number))

	print("Pool Rooms")

	for floor in range(floor_range):
		for room in range(ocean_view_room_range, pool_view_room_range):
			room_number = (int(str(floor + 1) + str(room + 1)))

			if (room + 1) % 2 == 0:
				print("inserting", room_number)
				cursor.execute(
				     """INSERT INTO room (view, type, room_number)
				        VALUES (%s, %s, %s);""",
				    ("pool", "double queen", room_number))
			else:
				print("inserting", room_number)
				cursor.execute(
				    """INSERT INTO room (view, type, room_number)
				        VALUES (%s, %s, %s);""",
				    ("pool", "single king", room_number))
	
	conn.commit()
	conn.close()

def db_connection():
	#Define our connection string
	conn_string = "host='localhost' dbname='ACME' user='postgres' password='password'"
 
	# print the connection string we will use to connect
	print("Connecting to database\n	->%s" % conn_string)
 
	# get a connection, if a connect cannot be made an exception will be raised here
	conn = psycopg2.connect(conn_string)
 
	# conn.cursor will return a cursor object, you can use this cursor to perform queries
	cursor = conn.cursor()
	print("Connected!\n")

	return (conn, cursor)

if __name__ == "__main__":
	insert_rooms()