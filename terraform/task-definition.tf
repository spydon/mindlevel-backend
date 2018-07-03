resource "aws_ecs_task_definition" "mindlevel" {
    family                = "mindlevel"
    container_definitions = <<DEFINITION
[
  {
    "name": "mindlevel",
    "image": "589361660625.dkr.ecr.eu-central-1.amazonaws.com/mindlevel:latest",
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8080,
        "hostPort": 8080
      }
    ],
    "memory": 987,
    "cpu": 1024
  }
]
DEFINITION
}

data "aws_ecs_task_definition" "mindlevel" {
  depends_on = [ "aws_ecs_task_definition.mindlevel" ]
  task_definition = "${aws_ecs_task_definition.mindlevel.family}"
}
