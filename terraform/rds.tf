resource "aws_db_subnet_group" "rds_sn_group" {
  name       = "rds_sn_group"
  subnet_ids = ["${aws_subnet.mindlevel_public_sn_01.id}",
                "${aws_subnet.mindlevel_public_sn_02.id}"]

  tags {
    Name = "DB subnet group"
  }
}

resource "aws_db_instance" "mindlevel" {
    depends_on                = [ "aws_vpc.mindlevel_vpc" ]
    identifier                = "mindlevel"
    allocated_storage         = 20
    storage_type              = "gp2"
    engine                    = "mariadb"
    engine_version            = "10.2.12"
    instance_class            = "db.t2.micro"
    name                      = "mindlevel"
    username                  = "root"
    password                  = "password" # Doesn't really matter, locked into VPC
    port                      = 3306
    publicly_accessible       = false
    availability_zone         = "eu-central-1a"
    security_group_names      = []
    vpc_security_group_ids    = ["${aws_security_group.mindlevel_vpc_sg.id}"]
    db_subnet_group_name      = "${aws_db_subnet_group.rds_sn_group.name}"
    parameter_group_name      = "${aws_db_parameter_group.mindlevel-mariadb.name}"
    multi_az                  = false
    backup_retention_period   = 0
    backup_window             = "20:16-20:46"
    maintenance_window        = "mon:23:24-mon:23:54"
    final_snapshot_identifier = "mindlevel-final"
}
