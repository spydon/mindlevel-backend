variable "ecs_cluster" {
  description = "ECS cluster name"
}

variable "ecs_key_pair_name" {
  description = "ECS key pair name"
}

variable "region" {
  description = "AWS region"
}

variable "availability_zone" {
  description = "availability zone used for the demo, based on region"
  default = {
    us-east-1 = "us-east-1"
  }
}

########################### MindLevel VPC Config ################################

variable "mindlevel_vpc" {
  description = "VPC for the MindLevel environment"
}

variable "mindlevel_network_cidr" {
  description = "IP addressing for the network"
}

variable "mindlevel_public_01_cidr" {
  description = "Public 0.0 CIDR for externally accessible subnet"
}

variable "mindlevel_public_02_cidr" {
  description = "Public 0.0 CIDR for externally accessible subnet"
}

########################### Autoscale Config ################################

variable "max_instance_size" {
  description = "Maximum number of instances in the cluster"
}

variable "min_instance_size" {
  description = "Minimum number of instances in the cluster"
}

variable "desired_capacity" {
  description = "Desired number of instances in the cluster"
}
