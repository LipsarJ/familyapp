    package org.example.messaging;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class InvitationEvent {
        private Long userId;
        private Long familyId;
        private String familyName;
        private String userEmail;
    }
