resource "aws_route53_zone" "mindlevel" {
    name       = "mindlevel.net"
    comment    = ""

    tags {
    }
}

resource "aws_route53_record" "mindlevel-net-NS" {
    zone_id = "${aws_route53_zone.mindlevel.zone_id}"
    name    = "mindlevel.net"
    type    = "NS"
    records = ["ns-98.awsdns-12.com.", "ns-811.awsdns-37.net.", "ns-1137.awsdns-14.org.", "ns-2001.awsdns-58.co.uk."]
    ttl     = "172800"
}

resource "aws_route53_record" "mindlevel-net-SOA" {
    zone_id = "${aws_route53_zone.mindlevel.zone_id}"
    name    = "mindlevel.net"
    type    = "SOA"
    records = ["ns-2001.awsdns-58.co.uk. awsdns-hostmaster.amazon.com. 1 7200 900 1209600 86400"]
    ttl     = "900"
}

resource "aws_route53_record" "api-mindlevel-net-A" {
    zone_id = "${aws_route53_zone.mindlevel.zone_id}"
    name    = "api.mindlevel.net"
    type    = "A"

    alias {
        name    = "${aws_alb.ecs-load-balancer.dns_name}"
        zone_id = "${aws_alb.ecs-load-balancer.zone_id}"
        evaluate_target_health = false
    }
}

resource "aws_route53_record" "api-mindlevel-net-AAAA" {
    zone_id = "${aws_route53_zone.mindlevel.zone_id}"
    name    = "api.mindlevel.net"
    type    = "AAAA"

    alias {
        name    = "${aws_alb.ecs-load-balancer.dns_name}"
        zone_id = "${aws_alb.ecs-load-balancer.zone_id}"
        evaluate_target_health = false
    }
}

resource "aws_route53_record" "ssh-mindlevel-net-A" {
    zone_id = "${aws_route53_zone.mindlevel.zone_id}"
    name    = "ssh.mindlevel.net"
    type    = "A"
    records = ["35.158.240.69"]
    ttl     = "300"
}
