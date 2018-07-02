
resource "aws_alb" "ecs-load-balancer" {
    name                = "ecs-load-balancer"
    security_groups     = ["${aws_security_group.mindlevel_public_sg.id}"]
    subnets             = ["${aws_subnet.mindlevel_public_sn_01.id}","${aws_subnet.mindlevel_public_sn_02.id}"]

    tags {
      Name = "ecs-load-balancer"
    }
}

resource "aws_alb_target_group" "ecs-target-group" {
    name                = "ecs-target-group"
    port                = "8080"
    protocol            = "HTTP"
    vpc_id              = "${aws_vpc.mindlevel_vpc.id}"

    health_check {
        healthy_threshold   = "5"
        unhealthy_threshold = "2"
        interval            = "30"
        matcher             = "200"
        path                = "/ping"
        port                = "traffic-port"
        protocol            = "HTTP"
        timeout             = "5"
    }

    tags {
      Name = "ecs-target-group"
    }
}

resource "aws_alb_listener" "alb-listener" {
    load_balancer_arn = "${aws_alb.ecs-load-balancer.arn}"
    port              = "443"
    protocol          = "HTTPS"
    ssl_policy	      =	"ELBSecurityPolicy-2016-08"
	certificate_arn	  = "arn:aws:acm:eu-central-1:589361660625:certificate/57648517-973e-4589-b740-b139631821a0"

    default_action {
        target_group_arn = "${aws_alb_target_group.ecs-target-group.arn}"
        type             = "forward"
    }
}
