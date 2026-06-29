// package com.example.springbook.user.service;

// import com.example.springbook.user.dao.UserDao;

// // import static com.example.springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
// // import static com.example.springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

// import com.example.springbook.user.domain.Level;
// import com.example.springbook.user.domain.User;

// public class UserLevelUpgradePolicyBase implements UserLevelUpgradePolicy {
//     // public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
//     // public static final int MIN_RECOMMEND_FOR_GOLD = 30;

//     UserDao userDao;

//     public void setUserDao(UserDao userDao){
//         this.userDao = userDao;
//     }

//     public boolean canUpgradeLevel(User user){
//         Level currentLevel = user.getLevel();
//         switch (currentLevel) {
//             case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
//             case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
//             case GOLD: return false;
//             default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
//         }
//     }

//     public void upgradeLevel(User user){
//         user.upgradeLevel();
//         userDao.update(user);
//     }
// }
