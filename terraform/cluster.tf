resource "aws_ecs_cluster" "mindlevel-ecs-cluster" {
    name = "${var.ecs_cluster}"
}
