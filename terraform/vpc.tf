#========================== MINDLEVEL VPC =============================

# Declare the data source
data "aws_availability_zones" "available" {}


# Define a vpc
resource "aws_vpc" "mindlevel_vpc" {
  cidr_block = "${var.mindlevel_network_cidr}"
  tags {
    Name = "${var.mindlevel_vpc}"
  }

  lifecycle {
    create_before_destroy = true
  }
}

# Internet gateway for the public subnet
resource "aws_internet_gateway" "mindlevel_ig" {
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"
  tags {
    Name = "mindlevel_ig"
  }
}

## Public subnet 1
resource "aws_subnet" "mindlevel_public_sn_01" {
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"
  cidr_block = "${var.mindlevel_public_01_cidr}"
  availability_zone = "${data.aws_availability_zones.available.names[0]}"
  tags {
    Name = "mindlevel_public_sn_01"
  }
}

# Routing table for public subnet 1
resource "aws_route_table" "mindlevel_public_sn_rt_01" {
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.mindlevel_ig.id}"
  }
  tags {
    Name = "mindlevel_public_sn_rt_01"
  }
}

# Associate the routing table to public subnet 1
resource "aws_route_table_association" "mindlevel_public_sn_rt_01_assn" {
  subnet_id = "${aws_subnet.mindlevel_public_sn_01.id}"
  route_table_id = "${aws_route_table.mindlevel_public_sn_rt_01.id}"
}

## Public subnet 2
resource "aws_subnet" "mindlevel_public_sn_02" {
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"
  cidr_block = "${var.mindlevel_public_02_cidr}"
  availability_zone = "${data.aws_availability_zones.available.names[1]}"
  tags {
    Name = "mindlevel_public_sn_02"
  }
}

# Routing table for public subnet 2
resource "aws_route_table" "mindlevel_public_sn_rt_02" {
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.mindlevel_ig.id}"
  }
  tags {
    Name = "mindlevel_public_sn_rt_02"
  }
}

# Associate the routing table to public subnet 2
resource "aws_route_table_association" "mindlevel_public_sn_rt_02_assn" {
  subnet_id = "${aws_subnet.mindlevel_public_sn_02.id}"
  route_table_id = "${aws_route_table.mindlevel_public_sn_rt_02.id}"
}

# ECS Instance Security group
resource "aws_security_group" "mindlevel_public_sg" {
  name = "mindlevel_public_sg"
  description = "Public access security group"
  vpc_id = "${aws_vpc.mindlevel_vpc.id}"

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"]
  }

  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"]
  }

  egress {
    # allow all traffic to private SN
    from_port = "0"
    to_port = "0"
    protocol = "-1"
    cidr_blocks = [
      "0.0.0.0/0"]
  }

  tags {
    Name = "mindlevel_public_sg"
  }
}
