package pl.rowerek.restriction;

import org.springframework.data.jpa.repository.JpaRepository;

interface RestrictionRepository extends JpaRepository<Restriction, RestrictionId> {
}
