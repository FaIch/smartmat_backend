import os
import mysql.connector
import csv
import time

print("Starting data loading script...")

MYSQL_USER = os.getenv("MYSQL_USER")
MYSQL_PASSWORD = os.getenv("MYSQL_PASSWORD")
MYSQL_DATABASE = os.getenv("MYSQL_DATABASE")

# Retry mechanism
max_retries = 10
retries = 0

while retries < max_retries:
    try:
        connection = mysql.connector.connect(
            host="db",
            user=MYSQL_USER,
            password=MYSQL_PASSWORD,
            database=MYSQL_DATABASE
        )
        print("Connected to the database.")
        break
    except mysql.connector.Error as e:
        print(f"Failed to connect to the database, retrying in 5 seconds... ({retries + 1}/{max_retries})")
        retries += 1
        time.sleep(5)
else:
    print("Failed to connect to the database after multiple retries. Exiting...")
    exit(1)

cursor = connection.cursor()

# Create the items table
cursor.execute("""
CREATE TABLE IF NOT EXISTS item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    base_amount DECIMAL(10, 2),
    category VARCHAR(255),
    image VARCHAR(255),
    name VARCHAR(255),
    price DECIMAL(10, 2),
    short_desc TEXT,
    unit VARCHAR(255),
    weight_per_unit DECIMAL(10, 2)
)
""")

# Create the recipes table
cursor.execute("""
CREATE TABLE IF NOT EXISTS recipe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    estimated_time VARCHAR(255),
    description TEXT,
    number_of_items INT,
    image MEDIUMBLOB
)
""")

# Create the recipe_items table
cursor.execute("""
CREATE TABLE IF NOT EXISTS recipe_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity DOUBLE,
    item_id INT,
    recipe_id INT,
    FOREIGN KEY (item_id) REFERENCES item (id),
    FOREIGN KEY (recipe_id) REFERENCES recipe (id)
)
""")


with open("/docker-entrypoint-initdb.d/items.csv", "r") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    cursor = connection.cursor()

    for row in reader:
        cursor.execute(
            "INSERT INTO item (id, base_amount, category, image, name, price, short_desc, unit, weight_per_unit) "
            "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)",
            (
                row["id"],
                row["base_amount"],
                row["category"],
                row["image"],
                row["name"],
                row["price"],
                row["short_desc"],
                row["unit"],
                row["weight_per_unit"],
            ),
        )

# Import recipes data
with open("/docker-entrypoint-initdb.d/recipes.csv", "r") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    cursor = connection.cursor()

    for row in reader:
        cursor.execute(
            "INSERT INTO recipe (id, name, estimated_time, description, number_of_items, image) "
            "VALUES (%s, %s, %s, %s, %s, %s)",
            (
                row["id"],
                row["name"],
                row["estimated_time"],
                row["description"],
                row["number_of_items"],
                row["image"],
            ),
        )


# Import recipe_items data
with open("/docker-entrypoint-initdb.d/recipe_items.csv", "r") as csvfile:
    reader = csv.DictReader(csvfile, delimiter=";")
    cursor = connection.cursor()

    for row in reader:
        cursor.execute(
            "INSERT INTO recipe_item (id, quantity, item_id, recipe_id) "
            "VALUES (%s, %s, %s, %s)",
            (
                row["id"],
                row["quantity"],
                row["item_id"],
                row["recipe_id"],
            ),
        )
    connection.commit()
    cursor.close()

connection.close()

