package top.iznauy.chinamobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iznauy.chinamobile.dao.*;
import top.iznauy.chinamobile.entity.PackagesOrder;
import top.iznauy.chinamobile.entity.PhoneData;
import top.iznauy.chinamobile.entity.PositivePhoneCall;
import top.iznauy.chinamobile.entity.User;
import top.iznauy.chinamobile.entity.packages.CurrentPackages;
import top.iznauy.chinamobile.entity.packages.PackageContent;
import top.iznauy.chinamobile.entity.packages.Packages;
import top.iznauy.chinamobile.entity.packages.SupportedPackages;
import top.iznauy.chinamobile.utils.Tuple;
import top.iznauy.chinamobile.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Example {

    private UserJPA userJPA;

    private PackageOrderJPA packageOrderJPA;

    private PackageContentJPA packageContentJPA;

    private CurrentPackagesJPA currentPackagesJPA;

    private PackagesJPA packagesJPA;

    private PhoneDataJPA phoneDataJPA;

    private PositivePhoneCallJPA positivePhoneCallJPA;

    private SupportedPackagesJPA supportedPackagesJPA;

    @Autowired
    public void setUserJPA(UserJPA userJPA) {
        this.userJPA = userJPA;
    }

    @Autowired
    public void setPackageOrderJPA(PackageOrderJPA packageOrderJPA) {
        this.packageOrderJPA = packageOrderJPA;
    }

    @Autowired
    public void setPackageContentJPA(PackageContentJPA packageContentJPA) {
        this.packageContentJPA = packageContentJPA;
    }

    @Autowired
    public void setCurrentPackagesJPA(CurrentPackagesJPA currentPackagesJPA) {
        this.currentPackagesJPA = currentPackagesJPA;
    }

    @Autowired
    public void setPackagesJPA(PackagesJPA packagesJPA) {
        this.packagesJPA = packagesJPA;
    }

    @Autowired
    public void setPhoneDataJPA(PhoneDataJPA phoneDataJPA) {
        this.phoneDataJPA = phoneDataJPA;
    }

    @Autowired
    public void setPositivePhoneCallJPA(PositivePhoneCallJPA positivePhoneCallJPA) {
        this.positivePhoneCallJPA = positivePhoneCallJPA;
    }

    @Autowired
    public void setSupportedPackagesJPA(SupportedPackagesJPA supportedPackagesJPA) {
        this.supportedPackagesJPA = supportedPackagesJPA;
    }

    public boolean subscribePackages(String phoneNumber, long packageId,
                                     PackagesOrder.PackagesOrderInForceType inForceType) {
        User user = userJPA.findById(phoneNumber).orElse(null);
        if (user == null) {
            System.out.println("unexisted phoenNumber");
            return false;
        }


        CurrentPackages currentPackages = currentPackagesJPA.findById(new CurrentPackages.CurrentPackagesKey(phoneNumber,
                packageId)).orElse(null);
        if (currentPackages != null) {
            System.out.println("Package has registered!");
            return false;
        }

        PackagesOrder order = new PackagesOrder(phoneNumber, packageId, new Date(), inForceType,
                PackagesOrder.PackagesOrderType.SUBSCRIBE);
        packageOrderJPA.saveAndFlush(order);

        if (inForceType == PackagesOrder.PackagesOrderInForceType.NOW) { // need extra process
            CurrentPackages newPackages = new CurrentPackages(phoneNumber, packageId);
            currentPackagesJPA.saveAndFlush(newPackages);
            List<PackageContent> packageContents = packageContentJPA.findByPackageId(packageId);
            System.out.println(packageContents);
            List<Packages> packagesList = new ArrayList<>();
            for (PackageContent content : packageContents) {
                PackageContent.PackageContentType type = content.getType();
                Packages packages = new Packages();

                packages.setAmount(content.getAmount());
                packages.setDate(new Date());
                packages.setPackageId(packageId);
                packages.setPhoneNumber(phoneNumber);
                packages.setType(type);

                packagesList.add(packages);
            }
            System.out.println(packageId);
            packagesJPA.saveAll(packagesList);
            packagesJPA.flush();
        }
        return true;
    }

    public boolean unSubscribePackages(String phoneNumber, long packageId,
                                       PackagesOrder.PackagesOrderInForceType inForceType) {
        User user = userJPA.findById(phoneNumber).orElse(null);
        if (user == null) {
            System.out.println("UnknownError");
            return false;
        }

        CurrentPackages currentPackages = currentPackagesJPA.findById(new CurrentPackages.CurrentPackagesKey(phoneNumber,
                packageId)).orElse(null);
        if (currentPackages == null) {
            System.out.println("UnknownError");
            return false;
        }

        if (inForceType == PackagesOrder.PackagesOrderInForceType.NOW) {
            List<PackageContent> packageContents = packageContentJPA.findByPackageId(packageId);
            List<Packages> packagesList = packagesJPA.findByPhoneNumberAndPackageIdAndDateIsAfter(phoneNumber, packageId,
                    Utils.getBeginDate());

            System.out.println(packagesList);
            boolean hasUsed = false;
            for (Packages packages : packagesList) {
                for (PackageContent content : packageContents) {
                    if (content.getType() == packages.getType() && content.getAmount() > packages.getAmount()) {
                        hasUsed = true;
                        break;
                    }
                }
                if (hasUsed)
                    break;
            }

            if (hasUsed) {
                System.out.println("UnknownError");
                return false;
            }
            packagesJPA.deleteAll(packagesList);
            packagesJPA.flush();
            System.out.println("UnknownError");
        }

        PackagesOrder order = new PackagesOrder(phoneNumber, packageId, new Date(), inForceType,
                PackagesOrder.PackagesOrderType.UN_SUBSCRIBE);
        packageOrderJPA.saveAndFlush(order);
        currentPackagesJPA.delete(new CurrentPackages(phoneNumber, packageId));

        return true;

    }

    public List<Packages> findPackages(String phoneNumber, int year, int month) {
        Date beginDate = Utils.getBeginDate(year, month);
        Date endDate = Utils.getEndDate(year, month);
        return packagesJPA.findByPhoneNumberAndDateIsBetween(phoneNumber, beginDate, endDate);
    }

    public double calculatePhoneDataFee(String phoneNumber, double amount, PhoneData.PhoneDataType type, Date begin,
                                        Date end) {
        User user = userJPA.findById(phoneNumber).orElse(null);
        if (user == null) {
            return 0.0;
        }

        PackageContent.PackageContentType contentType = PackageContent.PackageContentType.DOMESTIC_DATA;
        if (type == PhoneData.PhoneDataType.NATIVE)
            contentType = PackageContent.PackageContentType.NATIVE_DATA;

        List<Packages> packagesList = packagesJPA.findByPhoneNumberAndTypeAndDateIsAfter(phoneNumber,
                contentType, Utils.getBeginDate());

        double extraAmount = calculateExtraAmount(packagesList, amount);
        double fee;
        if (type == PhoneData.PhoneDataType.NATIVE)
            fee = extraAmount * FeeTable.NATIVE_DATA;
        else
            fee = extraAmount * FeeTable.DOMESTIC_DATA;

        packagesJPA.saveAll(packagesList);
        packagesJPA.flush();

        PhoneData phoneData = new PhoneData(phoneNumber, begin, end, amount, type, fee);
        phoneDataJPA.saveAndFlush(phoneData);

        if (extraAmount > 0) {
            if (type == PhoneData.PhoneDataType.NATIVE)
                user.addExtraNativeData(extraAmount);
            else
                user.addExtraDomesticData(extraAmount);
            userJPA.saveAndFlush(user);
        }

        return fee;
    }

    public double calculatePhoneCallFee(String phoneNumber, Date start, Date end, String receiver) {
        User user = userJPA.findById(phoneNumber).orElse(null);
        if (user == null)
            return 0.0;

        // calculate PhoneCall Time
        double minutes = (end.getTime() - start.getTime()) / 1000.0 / 60.0; 
        double standardMinutes = Math.ceil(minutes); 

        List<Packages> packagesList = packagesJPA.findByPhoneNumberAndTypeAndDateIsAfter(phoneNumber,
                PackageContent.PackageContentType.PHONE_CALL, Utils.getBeginDate());

        double extraTime = calculateExtraAmount(packagesList, standardMinutes);
        double fee = extraTime * FeeTable.PHONE_CALL;

        packagesJPA.saveAll(packagesList);
        packagesJPA.flush();

        PositivePhoneCall positivePhoneCall = new PositivePhoneCall(phoneNumber, receiver, start, end, fee);
        positivePhoneCallJPA.saveAndFlush(positivePhoneCall);

        if (extraTime > 0) {
            user.addExtraPhoneCallTime((int) fee);
            userJPA.saveAndFlush(user);
        }
        return fee;
    }

    private double calculateExtraAmount(List<Packages> packagesList, double total) {
        double last = total;
        for (Packages packages : packagesList) {
            double amount = packages.getAmount();
            if (amount > 0.0) {
                if (amount >= last) {
                    amount -= last;
                    packages.setAmount(amount);
                    last = 0;
                    break;
                } else {
                    last -= amount;
                    amount = 0;
                    packages.setAmount(amount);
                }
            }
        }
        return last;
    }
}
