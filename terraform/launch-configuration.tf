resource "aws_launch_configuration" "ecs-launch-configuration" {
    name                        = "ecs-launch-configuration"
    image_id                    = "ami-10e6c8fb"
    instance_type               = "t2.micro"
    iam_instance_profile        = "${aws_iam_instance_profile.ecs-instance-profile.id}"

    root_block_device {
      volume_type = "standard"
      volume_size = 8
      delete_on_termination = true
    }

    security_groups             = ["${aws_security_group.mindlevel_public_sg.id}",
                                   "${aws_security_group.mindlevel_vpc_sg.id}"]
    associate_public_ip_address = "true"
    key_name                    = "${var.ecs_key_pair_name}"
    user_data                   = <<EOF
                                  #!/bin/bash
                                  echo ECS_CLUSTER=${var.ecs_cluster} >> /etc/ecs/ecs.config
                                  EOF
}
