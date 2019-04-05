package com.mymoneyapp.backend.specification;

import com.mymoneyapp.backend.domain.BankingAccount;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Joins;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Joins({
        @Join(path = "user", alias = "u"),
})
@Conjunction(value = {}, and = {
        @Spec(path = "u.id", params = "user", spec = Equal.class),
})
public interface BankingAccountSpecification extends Specification<BankingAccount> {
}
