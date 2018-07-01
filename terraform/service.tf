resource "aws_ecs_service" "mindlevel-ecs-service" {
  	name            = "mindlevel-ecs-service"
  	iam_role        = "${aws_iam_role.ecs-service-role.name}"
  	cluster         = "${aws_ecs_cluster.mindlevel-ecs-cluster.id}"
  	task_definition = "${aws_ecs_task_definition.mindlevel.family}:${max("${aws_ecs_task_definition.mindlevel.revision}", "${data.aws_ecs_task_definition.mindlevel.revision}")}"
  	desired_count   = 1

  	load_balancer {
    	target_group_arn  = "${aws_alb_target_group.ecs-target-group.arn}"
    	container_port    = 8080
    	container_name    = "mindlevel"
	}
}
