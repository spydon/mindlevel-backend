resource "aws_s3_bucket" "mindlevel" {
    bucket = "mindlevel"
    acl    = "private"
    policy = <<POLICY
{
  "Version": "2012-10-17",
  "Id": "Policy1502134572156",
  "Statement": [
    {
      "Sid": "Stmt1502134565203",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::mindlevel/*"
    }
  ]
}
POLICY
}
