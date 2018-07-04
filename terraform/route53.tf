resource "aws_route53_record" "api-mindlevel-net-A" {
    zone_id = "${var.hosted_zone_id}"
    name    = "api.mindlevel.net"
    type    = "A"

    alias {
        name    = "${aws_alb.ecs-load-balancer.dns_name}"
        zone_id = "${aws_alb.ecs-load-balancer.zone_id}"
        evaluate_target_health = false
    }
}

resource "aws_route53_record" "api-mindlevel-net-AAAA" {
    zone_id = "${var.hosted_zone_id}"
    name    = "api.mindlevel.net"
    type    = "AAAA"

    alias {
        name    = "${aws_alb.ecs-load-balancer.dns_name}"
        zone_id = "${aws_alb.ecs-load-balancer.zone_id}"
        evaluate_target_health = false
    }
}

resource "aws_route53_record" "ssh-mindlevel-net-A" {
    zone_id = "${var.hosted_zone_id}"
    name    = "ssh.mindlevel.net"
    type    = "A"
    records = ["35.158.240.69"]
    ttl     = "300"
}

#
# Nameservers etc
#

resource "aws_route53_record" "mindlevel-net-NS-0" {
    zone_id = "${var.hosted_zone_id}"
    name    = "mindlevel.net"
    type    = "NS"
    records = ["ns-94.awsdns-11.com.", "ns-1416.awsdns-49.org.", "ns-693.awsdns-22.net.", "ns-1648.awsdns-14.co.uk."]
    ttl     = "900"
}

resource "aws_route53_record" "mindlevel-net-SOA-0" {
    zone_id = "${var.hosted_zone_id}"
    name    = "mindlevel.net"
    type    = "SOA"
    records = ["ns-1648.awsdns-14.co.uk. awsdns-hostmaster.amazon.com. 1 7200 900 1209600 86400"]
    ttl     = "900"
}