resource "aws_db_instance" "mindlevel" {
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
    db_subnet_group_name      = "ecs_to_rds"
    parameter_group_name      = "default.mariadb10.2"
    multi_az                  = false
    backup_retention_period   = 0
    backup_window             = "20:16-20:46"
    maintenance_window        = "mon:23:24-mon:23:54"
    final_snapshot_identifier = "mindlevel-final"
}
