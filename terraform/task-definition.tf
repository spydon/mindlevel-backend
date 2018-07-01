data "aws_ecs_task_definition" "mindlevel" {
  task_definition = "${aws_ecs_task_definition.mindlevel.family}"
}

resource "aws_ecs_task_definition" "mindlevel" {
    family                = "mindlevel"
    container_definitions = <<DEFINITION
[
  {
    "name": "mindlevel",
    "image": "mindlevel",
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8080,
        "hostPort": 8080
      }
    ],
    "memory": 900,
    "cpu": 2048
  }
]
DEFINITION
}
